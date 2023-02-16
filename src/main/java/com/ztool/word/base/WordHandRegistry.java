package com.ztool.word.base;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.SneakyThrows;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhangjianshan1992
 */
@Component
@Scope("singleton")
public class WordHandRegistry implements ApplicationContextAware, InitializingBean {

    private static final Map<String, WordDataHandlerAbstract> WORD_CMD_HANDLE_MAP = new ConcurrentHashMap<>();

    private static final Map<String, WordBasicDataHandlerAbstract> WORD_BASIC_HANDLE_MAP = new ConcurrentHashMap<>();

    private ApplicationContext context;

    @SneakyThrows
    @Override
    public void afterPropertiesSet() {

        Map<String, WordDataHandlerAbstract> wordCmdHandleMap = context.getBeansOfType(WordDataHandlerAbstract.class);
        for (Map.Entry<String, WordDataHandlerAbstract> handEntry : wordCmdHandleMap.entrySet()) {
            WordDataHandlerAbstract handle = handEntry.getValue();
            List<String> cmdCodeList = handle.dataCode();
            if (CollectionUtil.isNotEmpty(cmdCodeList)) {
                for (String cmdCode : cmdCodeList) {
                    WORD_CMD_HANDLE_MAP.put(cmdCode, handle);
                }
            }
        }

        Map<String, WordBasicDataHandlerAbstract> wordBasicHandleMap = context.getBeansOfType(WordBasicDataHandlerAbstract.class);
        for (Map.Entry<String, WordBasicDataHandlerAbstract> handEntry : wordBasicHandleMap.entrySet()) {
            WordBasicDataHandlerAbstract handle = handEntry.getValue();
            String planCode = handle.code();
            if (StringUtils.isNotBlank(planCode)) {
                WORD_BASIC_HANDLE_MAP.put(planCode, handle);
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    public static WordDataHandlerAbstract getDataHandler(String code) {
        return WORD_CMD_HANDLE_MAP.get(code);
    }

    public static WordBasicDataHandlerAbstract getBasicHandler(String code) {
        return WORD_BASIC_HANDLE_MAP.get(code);
    }
}
