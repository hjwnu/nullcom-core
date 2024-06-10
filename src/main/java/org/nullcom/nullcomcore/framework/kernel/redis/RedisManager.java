package org.nullcom.nullcomcore.framework.kernel.redis;

import java.time.Duration;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import org.nullcom.nullcomcore.component.AbstractComponent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@Component
public class RedisManager extends AbstractComponent {

  private final StringRedisTemplate stringRedisTemplate;

  private final RedisTemplate<String, Object> redisTemplate;

  @Value("${spring.data.redis.session.timeout:86400}")
  private int sessionTimeout;

  public RedisManager(StringRedisTemplate stringRedisTemplate, RedisTemplate<String, Object> redisTemplate) {
    this.stringRedisTemplate = stringRedisTemplate;
    this.redisTemplate = redisTemplate;
  }

  private void expire(String key) {
    redisTemplate.expire(key, this.sessionTimeout, TimeUnit.SECONDS);
  }

  private void expireString(String key) {
    stringRedisTemplate.expire(key, this.sessionTimeout, TimeUnit.SECONDS);
  }

  // -------------------------------------------------- String
  public boolean hasString(String key) {
    return Boolean.TRUE.equals(stringRedisTemplate.hasKey(key));
  }

  public void deleteString(String key) {
    stringRedisTemplate.delete(key);
  }

  public String getString(String key) {
    ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
    return valueOperations.get(key);
  }

  public void setString(String key, String value) {
    ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
    valueOperations.set(key, value);
    this.expireString(key);
  }

  public void setString(String key, String value, long duration) {
    ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
    Duration expireDuration = Duration.ofSeconds(duration);
    valueOperations.set(key, value, expireDuration);
  }

  public void setStringNoExpire(String key, String value) {
    ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
    Duration expireDuration = Duration.ofDays(1000000); // 4700년도쯤
    valueOperations.set(key, value, expireDuration);
  }

  public void renameString(String oldKey, String newKey) {
    stringRedisTemplate.rename(oldKey, newKey);
  }

  // -------------------------------------------------- common - redisTemplate
  public boolean hasObject(String key) {
    return Boolean.TRUE.equals(redisTemplate.hasKey(key));
  }

  public void deleteObject(String key) {
    redisTemplate.delete(key);
  }

  // -------------------------------------------------- Object
  public void setObject(String key, Object value, long duration) {
    ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
    Duration expireDuration = Duration.ofSeconds(duration);
    valueOperations.set(key, value, expireDuration);
  }

  public void setObject(String key, Object value) {
    ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
    valueOperations.set(key, value);
    this.expire(key);
  }

  public void setObjectNoExpire(String key, Object value) {
    ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
    Duration expireDuration = Duration.ofDays(1000000); // 4700년도쯤
    valueOperations.set(key, value, expireDuration);
  }

  public Object getObject(String key) {
    ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
    return valueOperations.get(key);
  }

  public void renameObject(String oldKey, String newKey) {
    redisTemplate.rename(oldKey, newKey);
  }

  // -------------------------------------------------- Set
  // public void setSet(String key, Object value){
  // SetOperations<String, Object> setOperations = redisTemplate.opsForSet();
  // setOperations.add(key, value);
  // }
  //
  // public Object getSet(String key){
  // SetOperations<String, Object> setOperations = redisTemplate.opsForSet();
  // return setOperations.members(key);
  // }

  // -------------------------------------------------- Map
  // TODO 확인해야 함
  public void setMapNoExpire(String key, String hashKey, Object value) {
    HashOperations<String, String, Object> hashOperations = redisTemplate.opsForHash();
    hashOperations.put(key, hashKey, value);
    // 4700년도쯤
    redisTemplate.expire(key, 1000000, TimeUnit.SECONDS);
  }

  public void setMap(String key, String hashKey, Object value) {
    HashOperations<String, String, Object> hashOperations = redisTemplate.opsForHash();
    hashOperations.put(key, hashKey, value);
    this.expire(key);
  }

  public Map<String, Object> getMap(String key) {
    HashOperations<String, String, Object> hashOperations = redisTemplate.opsForHash();
    return hashOperations.entries(key);
  }

  public Object getMap(String key, String hashKey) {
    HashOperations<String, String, Object> hashOperations = redisTemplate.opsForHash();
    return hashOperations.get(key, hashKey);
  }

  public void deleteMap(String key, String hashKey) {
    HashOperations<String, String, Object> hashOperations = redisTemplate.opsForHash();
    hashOperations.delete(key, hashKey);
  }

  public void deleteMap(String key) {
    HashOperations<String, String, Object> hashOperations = redisTemplate.opsForHash();
    Map<String, Object> allMap = hashOperations.entries(key);
    for (Entry<String, ?> set : allMap.entrySet()) {
      hashOperations.delete(key, set.getKey());
    }
  }

  // public void setMap(String key, String hk, Object value){
  // ListOperations<String, Object> ListOperations = redisTemplate.opsForList();
  // ListOperations.put(key, hk, value);
  // }

  public String generateKey(String... keys) {
    StringBuilder sb = new StringBuilder();

    for (String key : keys) {
      if (!sb.isEmpty()) {
        sb.append(":");
      }
      sb.append(key);
    }
    return sb.toString();
  }
}
