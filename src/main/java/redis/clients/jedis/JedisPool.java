package redis.clients.jedis;

import java.net.URI;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import redis.clients.util.Pool;

public class JedisPool extends Pool<Jedis> {

    public JedisPool(final GenericObjectPoolConfig poolConfig, final String host) {
	this(poolConfig, host, Protocol.DEFAULT_PORT, Protocol.DEFAULT_TIMEOUT,
		null, Protocol.DEFAULT_DATABASE, null);
    }

    public JedisPool(String host, int port) {
	this(new GenericObjectPoolConfig(), host, port,
		Protocol.DEFAULT_TIMEOUT, null, Protocol.DEFAULT_DATABASE, null);
    }

    public JedisPool(String host, int port, boolean ssl) {
	this(new GenericObjectPoolConfig(), host, port,
		Protocol.DEFAULT_TIMEOUT, null, Protocol.DEFAULT_DATABASE, null, ssl);
    }

    public JedisPool(final String host) {
	URI uri = URI.create(host);
	if (uri.getScheme() != null && (uri.getScheme().equals("redis") || uri.getScheme().equals("rediss"))) {
	    String h = uri.getHost();
	    int port = uri.getPort();
	    boolean ssl = uri.getScheme().equals("rediss");
	    String password = "";
	    if(uri.getUserInfo()!=null)
		    password = uri.getUserInfo().split(":", 2)[1];
	    int database = 0;
	    if(uri.getPath()!=null)
		    database = Integer.parseInt(uri.getPath().split("/", 2)[1]);
	    this.internalPool = new GenericObjectPool<Jedis>(
		    new JedisFactory(h, port, Protocol.DEFAULT_TIMEOUT,
			    password, database, null, ssl),
		    new GenericObjectPoolConfig());
	} else {
	    this.internalPool = new GenericObjectPool<Jedis>(new JedisFactory(
		    host, Protocol.DEFAULT_PORT, Protocol.DEFAULT_TIMEOUT,
		    null, Protocol.DEFAULT_DATABASE, null),
		    new GenericObjectPoolConfig());
	}
    }

    public JedisPool(final URI uri) {
	String h = uri.getHost();
	int port = uri.getPort();
	boolean ssl = uri.getScheme()!=null && uri.getScheme().equals("rediss");
	String password = "";
	if(uri.getUserInfo()!=null)
		password = uri.getUserInfo().split(":", 2)[1];
	int database = 0;
	if(uri.getPath()!=null)
		database = Integer.parseInt(uri.getPath().split("/", 2)[1]);
	this.internalPool = new GenericObjectPool<Jedis>(new JedisFactory(h,
		port, Protocol.DEFAULT_TIMEOUT, password, database, null, ssl),
		new GenericObjectPoolConfig());
    }

    public JedisPool(final GenericObjectPoolConfig poolConfig,
	    final String host, int port, int timeout, final String password) {
	this(poolConfig, host, port, timeout, password,
		Protocol.DEFAULT_DATABASE, null);
    }

    public JedisPool(final GenericObjectPoolConfig poolConfig,
	    final String host, final int port) {
	this(poolConfig, host, port, Protocol.DEFAULT_TIMEOUT, null,
		Protocol.DEFAULT_DATABASE, null);
    }

    public JedisPool(final GenericObjectPoolConfig poolConfig,
	    final String host, final int port, final int timeout) {
	this(poolConfig, host, port, timeout, null, Protocol.DEFAULT_DATABASE,
		null);
    }

    public JedisPool(final GenericObjectPoolConfig poolConfig,
	    final String host, int port, int timeout, final String password,
	    final int database) {
	this(poolConfig, host, port, timeout, password, database, null);
    }

    public JedisPool(final GenericObjectPoolConfig poolConfig,
	    final String host, int port, int timeout, final String password,
	    final int database, final String clientName) {
	super(poolConfig, new JedisFactory(host, port, timeout, password,
		database, clientName));
    }

    public JedisPool(final GenericObjectPoolConfig poolConfig,
    	    final String host, int port, int timeout, final String password,
    	    final int database, final String clientName, boolean ssl) {
    	super(poolConfig, new JedisFactory(host, port, timeout, password,
    		database, clientName, ssl));
        }
    
    public void returnBrokenResource(final Jedis resource) {
	if (resource != null) {
	    returnBrokenResourceObject(resource);
	}
    }

    public void returnResource(final Jedis resource) {
	if (resource != null) {
	    resource.resetState();
	    returnResourceObject(resource);
	}
    }
}
