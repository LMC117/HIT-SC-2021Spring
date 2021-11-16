package ADT.Interval;

import java.util.Set;

public interface IntervalSet<L> {

    /**
     * 创建一个空的IntervalSet.
     *
     * @param <L> L为IntervalSet标签的类型
     * @return 一个空的IntervalSet
     */
    public static <L> IntervalSet<L> empty() {
        return new  CommonIntervalSet<L>();
    }

    /**
     * 在当前的IntervalSet中插入新的时间段和标签
     *
     * @param start 时间段的起点
     * @param end   时间段的终点
     * @param label 时间段的标签
     */
    public void insert(long start, long end, L label);

    /**
     * 获得当前对象中的标签集合
     *
     * @return 当前对象中的标签集合
     */
    public Set<L> labels();

    /**
     * 从当前IntervalSet中移除某个标签所关联的时间段
     *
     * @param label 所要移除时间段的对应标签
     * @return 移除成功返回true，移除失败返回false
     */
    public boolean remove(L label);

    /**
     * 返回某个标签对应的时间段的开始时间
     *
     * @param label 时间段对应的标签
     * @return 返回开始时间,若没找到标签对应的时间段，则返回-1
     */
    public long start(L label);

    /**
     * 返回某个标签对应的时间段的结束时间
     *
     * @param label 时间段对应的标签
     * @return 返回结束时间，若没找到标签对应的时间段，则返回-1
     */
    public long end(L label);

    /**
     * 判断该IntervalSet是否为空
     *
     * @return 为空返回true，不为空返回false
     */
    public boolean isEmpty();

    /**
     * 判断时间轴是否存在空白
     *
     * @return 若存在空白，返回true；否则返回false
     */
    public boolean checkBlank();

    /**
     * 判断是否存在不同的 interval 之间有重叠
     *
     * @return 若存在不同的 interval 之间有重叠，返回true；否则返回false
     */
    public boolean checkOverlap();
}
