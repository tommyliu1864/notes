-- 导入common函数库
local common = require("common")
local read_http = common.read_http
local read_redis = common.read_redis

-- 导入cjson库
local cjson = require('cjson')
-- 导入共享词典，本地缓存
local item_cache = ngx.shared.item_cache

-- 封装查询函数
function read_data(key, expire, path, params)
	-- 查询本地缓存
	local val = item_cache:get(key)
	if not val then
		ngx.log(ngx.ERR, "本地缓存查询失败，尝试查询redis，key：", key)
		-- 查询redis缓存
		val = read_redis("43.133.192.187", 6379, key)		
		-- 判断查询结果
		if not val then
			ngx.log(ngx.ERR, "redis 查询失败，尝试查询http, key:", key)
			-- redis 查询失败，去查询http
			val = read_http(path, params)
		end
	end
	-- 查询成功，把数据写入本地缓存
	item_cache:set(key, val, expire)
	-- 返回数据
	return val
end

-- 获取路径参数
local id = ngx.var[1]

-- 查询商品信息
local itemJSON = read_data("item:id:".. id, 1800, "/item/".. id, nil)
-- 查询库存信息
local stockJSON = read_data("item:stock:id:".. id, 60, "/item/stock/".. id, nil)

-- JSON转化为lua的table
local item = cjson.decode(itemJSON)
local stock = cjson.decode(stockJSON)

-- 组合数据
item.stock = stock.stock
item.sold = stock.sold

-- 把item序列化为json 返回结果
ngx.say(cjson.encode(item))
