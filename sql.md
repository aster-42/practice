### sql grammar

ON DUPLICATE KEY UPDATE

UPDATE ROLLBACK_ON_FAIL TARGET_AFFECT_ROW 1 

### 


### case

limit 深度分页，业务限制查询深度
然后可以按照主键 id 排序，查询的时候传入 > id, 这样也可以起到优化效果  
查询量大，业务限制查询上限 (尤其是时间任务拉取数据)
历史数据量大, 对 db 压力大

left join, 左边与右边找关联，那 left join 前面的表要尽可能小
带上筛选条件后再 left join 可以改善性能
