package cn.itbat.microsoft.utils;

import cn.itbat.microsoft.vo.GraphUserVo;
import com.google.common.collect.Maps;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * @author mjj
 * @date 2020年08月07日 14:33:01
 */
public class CacheUtils {

    private static Map<String, List<GraphUserVo>> maps = Maps.newConcurrentMap();

    public static void put(String appName, List<GraphUserVo> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        maps.put(appName, list);

    }

    public static List<GraphUserVo> get(String appName) {
        return maps.get(appName);
    }
}
