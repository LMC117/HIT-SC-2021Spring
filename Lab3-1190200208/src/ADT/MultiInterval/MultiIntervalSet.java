package ADT.MultiInterval;

import ADT.Interval.IntervalSet;

import java.util.Set;

public interface MultiIntervalSet<L> {

    /**
     * 创建一个空的MultiIntervalSet.
     *
     * @param <L> L为MultiIntervalSet标签的类型
     * @return 一个空的MultiIntervalSet
     */
    public static <L> MultiIntervalSet<L> empty() {
        return new CommonMultiIntervalSet<L>();
    }

    /**
     * 判断该MultiIntervalSet是否为空
     *
     * @return 为空返回true，不为空返回false
     */
    public boolean isEmpty();

    /**
     * 在当前的multiIntervalSet中插入新的时间段和标签
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
     * 从当前MultiIntervalSet中移除某个标签所关联的所有时间段
     *
     * @param label 所要移除时间段的对应标签
     * @return 移除成功返回true，移除失败返回false
     */
    public boolean remove(L label);

    /**
     * 从当前对象中获取与某个标签所关联的所有时间段
     *
     * @param label 所要获取具体信息的时间段的对应标签
     * @return 返回IntervalSet<Integer>，其中的时间段按开始时间从小到大的次序排列
     */
    public IntervalSet<Integer> intervals(L label);

    /**
     * 判断时间轴是否允许空白
     *
     * @return 若存在空白，返回true；否则返回false
     */
    public boolean checkBlank();

    /**
     * 判断是否允许不同的 multiInterval 之间有重叠
     *
     * @return 若允许不同的 multiInterval 之间有重叠，返回true；否则返回false
     */
    public boolean checkOverlap();

    /**
     * 是否包含周期性的时间段
     *
     * @return 若包含周期性的时间段，则返回true；否则返回false
     */
    public boolean checkPeriodic();

    /**
     * 从当前MultiIntervalSet中移除某个标签所关联的特定时间段
     *
     * @param label 所要移除时间段的对应标签
     * @param start 所要移除时间段的起点
     * @return 移除成功返回true，移除失败返回false
     */
    public boolean removeSpecific(L label, long start);
}
