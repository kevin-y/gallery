# Introduction

Gallery is an open source demo project, anyone can copy and distribute it. If you want to use it for commercial product or any other purposes, it's okay, but without any guarantees. The initiative of making this demo is to separate things from my current work, but avail all of the skills I have obtained from 3 years of development.
<br>It's built on plenty of open source tools, such as AngularJS, Bootstrap, jQuery, Spring Framework, RabbitMQ, Redis, MySQL, MongoDB, Nginx and Tomcat etc.
<br>The reason why I don't officially give it a particular name is just because I have not come up with one yet, LOL.
Well, I'll attempt to integrate more tools to provide powerful functionalities in the future.
Please wait, :P!!!
<br>Below are some architectural perspectives:
<br>**Architecture:**

![Architecture](https://github.com/kevin-y/gallery-docs/blob/master/Design/Architecture.png "Architecture")

<br>**Physical Topology:**

![Physical Topology](https://github.com/kevin-y/gallery-docs/blob/master/Design/physical_topology.png "Physical Topology")

<br>More information please see the **[gallery-docs](https://github.com/kevin-y/gallery-docs)** project, it includes design, API and help docs etc. 
<br> Frontend project please see **[gallery-ui](https://github.com/kevin-y/gallery-ui)**.

# Setup & Installation

## For windows
It's pretty easy to run all the services on windows OS.
**Requirements**
<ol>
	<li>Windows XP, Vista, 7, 8, 8.1 and 10, both are okay, I'm using windows 7</li>
	<li>Java JDK 1.6+, 1.7+ is recommended, because I will explore some features that only come up with 1.7.</li>
	<li><a href="https://www.mongodb.org/downloads" target="_blank">MongoDB</a> , latest version is recommended, because it has a lot of optimizations, but be aware there are two storage engines, <b>MMAPv1</b> and <b>WiredTiger</b>, they're not compatible with each other. The former is available in previous versions, and the later only in 3.0. I am currently using 2.6.x and will later switch to 3.0 and try WiredTiger. Well, 3.0 has a finer granularity of locks. See <a href="http://docs.mongodb.org/manual/release-notes/3.0/" target="_blank">Release Notes for MongoDB</a>. </li>
	<li>Erlang runtime enviroment, OTP 16B02+</li>
	<li>RabbitMQ server, just use the latest version</li>
	<li>Redis key-value server, well, redis windows version is not recommended for running in production environments, but for development purpose, it would not cause too much harm. I am using 2.18.19.1, see <a href="https://github.com/MSOpenTech/redis/releases" target="_blank">Redis 2.18.19.1</a></li>
	<li>Tomcat 7.x+</li>
	<li>MySQL 5.x, the latest version is recommended</li>
	<li>Development tools, I am using maven to manage the project, so any IDE supports maven will work fine.</li>
</ol> 

## For Linux
The requirements are the same as on windows platforms.

## Install Nginx (Ubuntu 12.04 64bit)

Add key for the apt program:
<pre><code>
$ wget http://nginx.org/keys/nginx_signing.key
$ sudo apt-key add nginx_signing.key
</code></pre>

Add repositories to the /etc/apt/sources.list file:
<pre><code>
$ sudo vim /etc/apt/sources.list
</code></pre>

> deb http://nginx.org/packages/ubuntu/ precise nginx<br>
> deb-src http://nginx.org/packages/ubuntu/ precise nginx

Execute the following commands will install the latest version: 
<pre><code>
$ sudo apt-get update
$ sudo apt-get install nginx
</code></pre>

BTW, the command below is to check nginx version:
<pre><code>
$ sudo apt-cache showpkg nginx
</code></pre>

Well, when the installation process is done, you could use the following command to run nginx server:
<pre><code>
$ sudo service nginx start
</code></pre>

If no error shows up, then hooray, you have successfully installed nginx server. Type `localost` in the browser address field, you should see a welcome page!

Now, configure nginx for your good. Edit `default.conf`:
<pre><code>
$ sudo vim /etc/nginx/conf.d/default.conf
</code></pre>

Modify <b>location</b> bloc as follows:  
>  location / {<br>
>        root   /path/to/gallery-ui;<br>
>        index  index.html index.htm;<br>
>  }<br>

Then restart nginx:
<pre><code>
$ sudo service nginx restart
</code></pre>

Try accessing `localhost` with your browser, you should see the following page.

![Gallery Main Page](https://github.com/kevin-y/gallery-docs/blob/master/resources/images/gallery_main_page.png "Gallery Main Page")
 
## Install MySQL
Installing MySQL is pretty easy, just install through **apt repository**:
<pre><code>
$ sudo apt-get install -y mysql-server
</code></pre>

The following command is to check whether MySQL server is running on port 3306(by default):
<pre><code>
$ sudo lsof -i:3306
</code></pre>


> COMMAND   PID  USER   FD   TYPE DEVICE SIZE/OFF NODE NAME <br>
> mysqld  15580 mysql   10u  IPv4  63778      0t0  TCP localhost:mysql (LISTEN)

## Install Redis
Before we start compiling redis source, we need to install gcc compiler if we haven't had one. The latest version is recommended. Here I am gonna install directly from the apt repository.
<pre><code>
$ sudo apt-get -y install gcc 
</code></pre>

Now, we begin to compile and install redis:
<pre><code>
$ wget http://download.redis.io/releases/redis-3.0.1.tar.gz
$ tar -zxvf redis-3.0.1.tar.gz
$ cd redis-3.0.1/
$ cd deps && make hiredis lua jemalloc linenoise
$ cd ..
$ make
</code></pre>

If there's no error during compilation, then we could install and configure redis now:
<pre><code>
$ sudo mkdir /etc/redis
$ sudo cp redis.conf /etc/redis/
$ sudo mkdir -p /usr/local/redis/bin
$ sudo cp src/redis-* /usr/local/redis/bin/
$ sudo sysctl vm.overcommit_memory=1
$ sudo groupadd redis
$ sudo useradd -r -g redis redis
$ sudo chown -R redis:redis /usr/local/redis/
$ sudo mkdir /var/log/redis/
$ sudo chown -R redis:redis /var/log/redis/
</code></pre>

Configure redis environment:
<pre><code>
$ sudo vim /etc/profile

# Add the following lines:
# Redis Environment
REDIS_HOME=/usr/local/redis
export PATH=$REDIS_HOME/bin:$PATH

# makes it being effective immediately
$ su - 
# source /etc/profile  #You may need root to run redis server
</code></pre>

Comment out `save` directives in `redis.conf` to disable persistence mechanism, because we just use redis as a cache server, it's okay if you don't do this:
<pre><code>
$ sudo vim /etc/redis/redis.conf

# turn off save directives
#save 900 1 <br>
#save 300 10 <br>
#save 60 10000 <br>

# change log file location
logfile /var/log/redis/redis.log <br>
</code></pre>



Run server:
<pre><code>
$ su -
# redis-server /etc/redis/redis.conf
</code></pre>

Connect to the server:
<pre><code>
$ redis-cli -p 6379
127.0.0.1:6379> 
</code></pre>

## Install RabbitMQ
<pre><code>
$ sudo echo "deb http://www.rabbitmq.com/debian/ testing main" >> /etc/apt/sources.list
$ wget https://www.rabbitmq.com/rabbitmq-signing-key-public.asc
$ sudo apt-key add rabbitmq-signing-key-public.asc
$ sudo apt-get update
$ sudo apt-get install rabbitmq-server
</code></pre>

## Install MongoDB
Just follow along with this tutorial: 
[Install MongoDB on Ubuntu](http://docs.mongodb.org/master/tutorial/install-mongodb-on-ubuntu/)

<blockquote>
 <b>Note:</b><br>
It may have a warning message saying: WARNING: /sys/kernel/mm/transparent_hugepage/defrag is 'always'<br>

You can eliminate this message by following these commands:<br>

$ su - <br>
# echo never > /sys/kernel/mm/transparent_hugepage/defrag <br>
</blockquote>

## Install Tomcat 7.x+
Just download the binary archive from [Apache Tomcat](http://tomcat.apache.org/download-70.cgi), and extract it to your desired directory.


