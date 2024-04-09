package com.heima.wemedia.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.heima.apis.article.IAritcleClient;
import com.heima.model.article.dtos.ArticleDto;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.pojos.WmChannel;
import com.heima.model.wemedia.pojos.WmNews;
import com.heima.model.wemedia.pojos.WmSensitive;
import com.heima.model.wemedia.pojos.WmUser;
import com.heima.utils.common.SensitiveWordUtil;
import com.heima.wemedia.mapper.WmChannelMapper;
import com.heima.wemedia.mapper.WmNewsMapper;
import com.heima.wemedia.mapper.WmSensitiveMapper;
import com.heima.wemedia.mapper.WmUserMapper;
import com.heima.wemedia.service.WeNewsAutoScanService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: 周海
 * @Create : 2024/3/27
 **/
@Service
@Slf4j
public class WeNewsAutoScanServiceImpl implements WeNewsAutoScanService {
    @Autowired
    private WmUserMapper wmUserMapper;

    @Autowired
    private WmChannelMapper wmChannelMapper;
    @Autowired
    private IAritcleClient iAritcleClient;

    @Autowired
    private WmNewsMapper wmNewsMapper;

    /**
     * 自媒体文章审核
     *
     * @param id 自媒体文章id
     */
    @Override
    public void autoWmnews(Integer id) {
        //查询文章 -- 自媒体文章
        WmNews wmNews = wmNewsMapper.selectById(id);
        if (wmNews == null) {
            throw new RuntimeException("WeNewsAutoScanServiceImpl-文章不存在");
        }
        if (wmNews.getStatus().equals(WmNews.Status.SUBMIT.getCode())) {
            //获取文本和图片内容
            Map<String, Object> textandImages = handerTextAndImages(wmNews);
            //TODO:审核文本内容--上传到阿里云的文本内容审核--跳过这一步--直接人工审核或者是不审核
            //TODO:自管理的文章敏感词过滤，这里没有实现阿里云的接口
            boolean isTextScan = handlerSensitiveScan((String) textandImages.get("content"), wmNews);
            //审核成功 保存app端的相关文章数据
            ResponseResult responseResult = saveAppArticle(wmNews);
            if (!responseResult.getCode().equals(200)) {
                throw new RuntimeException("WeNewsAutoScanServiceImpl-文章审核，保存App端相关文章失败");
            }
            wmNews.setArticleId((Long) responseResult.getData());
        }


    }

    @Autowired
    private WmSensitiveMapper wmSensitiveMapper;

    /**
     * 自管理的敏感词
     *
     * @param content
     * @param wmNews
     * @return
     */
    private boolean handlerSensitiveScan(String content, WmNews wmNews) {
        boolean flag = true;
        //1.获取所有敏感词
        List<WmSensitive> wmSensitiveList = wmSensitiveMapper.selectList(Wrappers.<WmSensitive>lambdaQuery().select(WmSensitive::getSensitives));
        List<String> senstivelist = wmSensitiveList.stream().map(WmSensitive::getSensitives).collect(Collectors.toList());
        //2.初始化词库
        SensitiveWordUtil.initMap(senstivelist);
        //3.查看文章中是否包含敏感词
        Map<String, Integer> map = SensitiveWordUtil.matchWords(content);
        if (map.size() > 0) {
            updateWmNews(wmNews, (short) 2, "当前文章中存在违规内容" + map);
            flag = false;
        }
        return flag;
    }

    /**
     * 修改文章内容
     *
     * @param wmNews
     * @param status
     * @param reason
     */
    private void updateWmNews(WmNews wmNews, short status, String reason) {
        wmNews.setStatus(status);
        wmNews.setReason(reason);
        wmNewsMapper.updateById(wmNews);
    }


    /**
     * 提取图片和内容
     *
     * @param wmNews
     * @return
     */
    private Map<String, Object> handerTextAndImages(WmNews wmNews) {

        StringBuilder stringBuilder = new StringBuilder();
        List<String> imglist = new ArrayList<>();

        if (StringUtils.isNotBlank(wmNews.getContent())) {
            List<Map> maps = JSONArray.parseArray(wmNews.getContent(), Map.class);
            for (Map map : maps) {
                if (map.get("type").equals("text")) {
                    stringBuilder.append(map.get("value"));
                }
                if (map.get("type").equals("inage")) {
                    imglist.add((String) map.get("value"));
                }
            }
            if (StringUtils.isNotBlank(wmNews.getImages())) {
                String[] split = wmNews.getImages().split(",");
                imglist.addAll(Arrays.asList(split));
            }
        }
        HashMap<String, Object> result = new HashMap<>();
        result.put("content", stringBuilder.toString());
        result.put("images", imglist);
        return result;
    }

    /**
     * 保存app端的文章数据
     *
     * @param wmNews
     */
    private ResponseResult saveAppArticle(WmNews wmNews) {
        ArticleDto dto = new ArticleDto();
        BeanUtils.copyProperties(wmNews, dto);
        dto.setLayout(wmNews.getType());
        WmChannel wmChannel = wmChannelMapper.selectById(wmNews.getChannelId());

        if (wmChannel != null) {
            dto.setChannelName(wmChannel.getName());
        }
        dto.setAuthorId(Long.valueOf(wmNews.getUserId()));
        WmUser wmUser = wmUserMapper.selectById(wmNews.getUserId());
        //设置作者姓名
        if (wmUser != null) {
            dto.setAuthorName(wmUser.getName());
        }
        //设置文章id
        if (wmNews.getArticleId() != null) {
            dto.setId(wmNews.getArticleId());
        }
        dto.setCreatedTime(new Date());

        ResponseResult responseResult = iAritcleClient.saveArticle(dto);
        return responseResult;

    }
}

