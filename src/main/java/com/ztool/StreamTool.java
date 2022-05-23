package com.ztool;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author zhangjianshan on 2021-07-27
 * @since 1.8
 */
@SuppressWarnings("unchecked")
public class StreamTool {

    public static final String SPLIT = ",";

    public static final char DEFAULT_SPLIT = ',';


    /**
     * 切分字符串
     *
     * @param str       被切分的字符串
     * @param separator 分隔符字符
     * @return 切分后的数组
     */
    public static <R> List<R> splitToList(CharSequence str, char separator, Function<? super String, ? extends R> keyMapper) {
        return Arrays.stream(StrUtil.splitToArray(str, separator)).map(keyMapper).collect(Collectors.toList());
    }

    /**
     * 切分字符串
     *
     * @param str 被切分的字符串（默认分隔符字符,）
     * @return 切分后的数组
     */
    public static <R> List<R> splitToList(CharSequence str, Function<? super String, ? extends R> keyMapper) {
        return splitToList(str, DEFAULT_SPLIT, keyMapper);
    }

    /**
     * 切分字符串为数字流
     *
     * @param str       被切分的字符串
     * @param separator 分隔符字符
     * @return 切分后的数组
     */
    public static Stream<String> splitToStream(CharSequence str, char separator) {
        return Arrays.stream(StrUtil.splitToArray(str, separator));
    }

    public static List<Integer> splitToIntList(CharSequence str) {
        return splitToIntList(str, DEFAULT_SPLIT);
    }

    public static List<Integer> splitToIntList(CharSequence str, char separator) {
        return splitToList(str, separator, Integer::new);
    }

    /**
     * list 排序分组
     *
     * @param dataList      dataList
     * @param sortMapper    排序字段
     * @param groupByMapper 分组字段
     * @return 分组排序后的map
     */
    public static <T, K, U extends Comparable<? super U>> Map<K, List<T>> groupByOrder(List<T> dataList, Function<? super T, ? extends U> sortMapper, Function<? super T, ? extends K> groupByMapper) {
        return dataList.stream().sorted(Comparator.comparing(sortMapper, Comparator.nullsLast(Comparator.reverseOrder()))).collect(Collectors.groupingBy(groupByMapper, LinkedHashMap::new, Collectors.toList()));
    }

    /**
     * 根据多个字段分组
     *
     * @param dataList dataList
     * @param keyFun   分组字段
     * @return 分组后map
     */
    public static <T, K> Map<String, List<T>> groupingByMoreKey(List<T> dataList, Function<? super T, ? extends K>... keyFun) {
        Map<String, List<T>> dataMap = new HashMap<>();
        for (T t : dataList) {
            StringJoiner keyJoiner = new StringJoiner(SPLIT);
            for (Function<? super T, ? extends K> function : keyFun) {
                K apply = function.apply(t);
                keyJoiner.add(apply + "");
            }
            String key = keyJoiner.toString();
            List<T> valueList = dataMap.getOrDefault(key, new ArrayList<>());
            valueList.add(t);
            dataMap.put(key, valueList);
        }
        return dataMap;
    }

    public static <K, T> List<K> toDistinctList(List<T> dataList, Function<? super T, ? extends K> keyMapper) {
        return toList(dataList, keyMapper).stream().distinct().collect(Collectors.toList());
    }

    public static <K, T> String join(List<T> dataList, Function<? super T, ? extends K> keyMapper, CharSequence conjunction) {
        return CollectionUtil.join(toList(dataList, keyMapper).stream().distinct().collect(Collectors.toList()), conjunction);
    }


    public static <K, T> List<K> toList(List<T> dataList, Function<? super T, ? extends K> keyMapper) {
        return dataList.stream().filter(Objects::nonNull).map(keyMapper).collect(Collectors.toList());
    }

    public static <T, U extends Comparable<? super U>> List<T> distinct(List<T> dataList, Function<? super T, ? extends U> keyExtractor) {
        return dataList.stream().filter(Objects::nonNull).collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(keyExtractor))), ArrayList::new));
    }


    public static <T, U extends Comparable<? super U>> T max(List<T> dataList, Function<? super T, ? extends U> keyExtractor) {
        return dataList.stream().filter(Objects::nonNull).max(Comparator.comparing(keyExtractor)).orElse(null);
    }

    public static <T, U extends Comparable<? super U>> T min(List<T> dataList, Function<? super T, ? extends U> keyExtractor) {
        return dataList.stream().filter(Objects::nonNull).min(Comparator.comparing(keyExtractor)).orElse(null);
    }

    public static <T, U extends Comparable<? super U>> T max(List<T> dataList, Function<? super T, ? extends U> keyExtractor, Comparator<? super U> keyComparator) {
        return dataList.stream().filter(Objects::nonNull).max(Comparator.comparing(keyExtractor, Comparator.nullsFirst(keyComparator))).orElse(null);
    }

    public static <T, U extends Comparable<? super U>> T min(List<T> dataList, Function<? super T, ? extends U> keyExtractor, Comparator<? super U> keyComparator) {
        return dataList.stream().filter(Objects::nonNull).min(Comparator.comparing(keyExtractor, Comparator.nullsLast(keyComparator))).orElse(null);
    }


    /**
     * 默认为 equals hashcode去重
     *
     * @param dataList dataList
     * @param <T>      obj
     * @return 去重后的dataList
     */
    public static <T> List<T> distinct(List<T> dataList) {
        return dataList.stream().distinct().collect(Collectors.toList());
    }

    public static <T, U extends Comparable<? super U>> List<T> sorted(List<T> dataList, Function<? super T, ? extends U> keyExtractor) {
        return dataList.stream().filter(Objects::nonNull).sorted(Comparator.comparing(keyExtractor)).collect(Collectors.toList());
    }

    public static <T, U extends Comparable<? super U>> List<T> sorted(List<T> dataList, Function<? super T, ? extends U> keyExtractor, boolean reversed) {
        if (reversed) {
            return dataList.stream().sorted(Comparator.comparing(keyExtractor).reversed()).collect(Collectors.toList());
        }
        return sorted(dataList, keyExtractor);
    }

    public static <T, U extends Comparable<? super U>> List<T> sorted(List<T> dataList) {
        return dataList.stream().filter(Objects::nonNull).sorted().collect(Collectors.toList());
    }

    public static <T extends Comparable<? super T>> List<T> sorted(List<T> dataList, boolean reversed) {
        if (reversed) {
            return dataList.stream().filter(Objects::nonNull).sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        }
        return sorted(dataList);
    }

    public static <T, K, V> Map<K, V> merge(List<T> dataList, Function<? super T, ? extends K> key, Function<? super T, ? extends V> value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        Map<K, V> dataMap = new HashMap<>();
        dataList.forEach((t) -> {
            dataMap.merge(key.apply(t), value.apply(t), remappingFunction);
        });
        return dataMap;
    }


    public static <K, T> Map<K, T> toMap(List<T> dataList, Function<? super T, ? extends K> keyMapper) {

        return dataList.stream().filter(Objects::nonNull).collect(toMap(keyMapper));
    }

    public static <T, K, U> Collector<T, ?, Map<K, U>> toMap(Function<? super T, ? extends K> keyMapper) {
        Function<? super T, ? extends U> valueMapper = t -> (U) t;
        return toMap(keyMapper, valueMapper, merger(), HashMap::new);
    }


    public static <T, K, U> Collector<T, ?, Map<K, U>> toMap(Function<? super T, ? extends K> keyMapper,
                                                             Function<? super T, ? extends U> valueMapper) {
        return toMap(keyMapper, valueMapper, merger(), HashMap::new);
    }

    static final Set<Collector.Characteristics> CH_ID = Collections.unmodifiableSet(EnumSet.of(Collector.Characteristics.IDENTITY_FINISH));

    public static <T, K, U, M extends Map<K, U>> Collector<T, ?, M> toMap(Function<? super T, ? extends K> keyMapper,
                                                                          Function<? super T, ? extends U> valueMapper,
                                                                          BinaryOperator<U> mergeFunction,
                                                                          Supplier<M> mapSupplier) {
        BiConsumer<M, T> accumulator
                = (map, element) -> map.merge(keyMapper.apply(element),
                valueMapper.apply(element), mergeFunction);
        return new CollectorImpl<>(mapSupplier, accumulator, mapMerger(mergeFunction), CH_ID);
    }

    private static <T> BinaryOperator<T> merger() {
        return (u, v) -> v;
    }

    private static <K, V, M extends Map<K, V>> BinaryOperator<M> mapMerger(BinaryOperator<V> mergeFunction) {
        return (m1, m2) -> {
            for (Map.Entry<K, V> e : m2.entrySet())
                m1.merge(e.getKey(), e.getValue(), mergeFunction);
            return m1;
        };
    }

    static class CollectorImpl<T, A, R> implements Collector<T, A, R> {
        private final Supplier<A> supplier;
        private final BiConsumer<A, T> accumulator;
        private final BinaryOperator<A> combiner;
        private final Function<A, R> finisher;
        private final Set<Characteristics> characteristics;

        CollectorImpl(Supplier<A> supplier,
                      BiConsumer<A, T> accumulator,
                      BinaryOperator<A> combiner,
                      Function<A, R> finisher,
                      Set<Characteristics> characteristics) {
            this.supplier = supplier;
            this.accumulator = accumulator;
            this.combiner = combiner;
            this.finisher = finisher;
            this.characteristics = characteristics;
        }

        CollectorImpl(Supplier<A> supplier,
                      BiConsumer<A, T> accumulator,
                      BinaryOperator<A> combiner,
                      Set<Characteristics> characteristics) {
            this(supplier, accumulator, combiner, castingIdentity(), characteristics);
        }

        @Override
        public BiConsumer<A, T> accumulator() {
            return accumulator;
        }

        @Override
        public Supplier<A> supplier() {
            return supplier;
        }

        @Override
        public BinaryOperator<A> combiner() {
            return combiner;
        }

        @Override
        public Function<A, R> finisher() {
            return finisher;
        }

        @Override
        public Set<Characteristics> characteristics() {
            return characteristics;
        }
    }


    private static <I, R> Function<I, R> castingIdentity() {
        return i -> (R) i;
    }
}
