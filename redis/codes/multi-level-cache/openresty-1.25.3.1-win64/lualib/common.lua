-- ����redis
local redis = require('resty.redis')
-- ��ʼ��redis
local red = redis:new()
red:set_timeouts(1000, 1000, 1000)

-- �ر�redis���ӵĹ��߷�������ʵ�Ƿ������ӳ�
local function close_redis(red)
    local pool_max_idle_time = 10000 -- ���ӵĿ���ʱ�䣬��λ�Ǻ���
    local pool_size = 100 --���ӳش�С
    local ok, err = red:set_keepalive(pool_max_idle_time, pool_size)
    if not ok then
        ngx.log(ngx.ERR, "����redis���ӳ�ʧ��: ", err)
    end
end

-- ��ѯredis�ķ��� ip��port��redis��ַ��key�ǲ�ѯ��key
local function read_redis(ip, port, key)
    -- ��ȡһ������
    local ok, err = red:connect(ip, port)
    if not ok then
        ngx.log(ngx.ERR, "����redisʧ�� : ", err)
        return nil
    end
    -- ��ѯredis
    local resp, err = red:get(key)
    -- ��ѯʧ�ܴ���
    if not resp then
        ngx.log(ngx.ERR, "��ѯRedisʧ��: ", err, ", key = " , key)
    end
    --�õ�������Ϊ�մ���
    if resp == ngx.null then
        resp = nil
        ngx.log(ngx.ERR, "��ѯRedis����Ϊ��, key = ", key)
    end
    close_redis(red)
    return resp
end

-- ��װ����������http���󣬲�������Ӧ
local function read_http(path, params)
    local resp = ngx.location.capture(path,{
        method = ngx.HTTP_GET,
        args = params,
    })
    if not resp then
        -- ��¼������Ϣ������404
        ngx.log(ngx.ERR, "http�����ѯʧ��, path: ", path , ", args: ", args)
        ngx.exit(404)
    end
    return resp.body
end
-- ����������
local _M = {  
    read_http = read_http,
	read_redis = read_redis
}  
return _M
