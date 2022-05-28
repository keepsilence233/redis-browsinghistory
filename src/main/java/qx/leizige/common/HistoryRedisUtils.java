package qx.leizige.common;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

@Component
public class HistoryRedisUtils {

	@Resource
	private RedisTemplate<String, Object> redisTemplate;

	@Resource
	private ZSetOperations<String, String> zSetOperations;


	/**
	 * 添加一个元素
	 */
	public boolean add(String key, String value, double score) {
		return zSetOperations.add(key, value, score);
	}


	/**
	 * 查询集合中指定顺序的值  zrevrange
	 * <p>
	 * 返回有序的集合中，score大的在前面
	 */
	public Set<String> reverseRangeByScore(String key, int offset, int count) {
		return zSetOperations.reverseRangeByScore(key, 1, Long.MAX_VALUE, (long) (offset - 1) * count, count);
	}

	/**
	 * ZCARD key
	 * <p>
	 * 返回有序集 key 的基数。
	 * <p>
	 * 可用版本：
	 * >= 1.2.0
	 * 时间复杂度:
	 * O(1)
	 * 返回值:
	 * 当 key 存在且是有序集类型时，返回有序集的基数。
	 * 当 key 不存在时，返回 0
	 */
	public Long zCard(String key) {
		return zSetOperations.zCard(key);
	}

	/**
	 * 删除元素 zrem
	 *
	 * @param key
	 * @param value
	 */
	public Long remove(String key, String value) {
		return zSetOperations.remove(key, value);
	}


	/**
	 * 移除有序集 key 中，指定排名(rank)区间内的所有成员。
	 * <p>
	 * 区间分别以下标参数 start 和 stop 指出，包含 start 和 stop 在内。
	 * <p>
	 * 下标参数 start 和 stop 都以 0 为底，也就是说，以 0 表示有序集第一个成员，以 1 表示有序集第二个成员，以此类推。
	 * 你也可以使用负数下标，以 -1 表示最后一个成员， -2 表示倒数第二个成员，以此类推。
	 * 可用版本：
	 * >= 2.0.0
	 * 时间复杂度:
	 * O(log(N)+M)， N 为有序集的基数，而 M 为被移除成员的数量。
	 *
	 * @param key
	 * @param start
	 * @param end
	 * @return 被移除成员的数量
	 */
	public Long removeRange(String key, long start, long end) {
		return zSetOperations.removeRange(key, start, end);
	}

	/**
	 * <p>
	 * 为给定 key 设置生存时间，当 key 过期时(生存时间为 0 )，它会被自动删除。
	 * </p>
	 *
	 * @param key
	 * @param value 过期时间，单位：秒
	 * @return 成功返回true
	 */
	public Boolean expire(String key, int value) {
		return redisTemplate.expire(key, value, TimeUnit.SECONDS);
	}

	public void delete(String key) {
		this.redisTemplate.delete(key);
	}

}
