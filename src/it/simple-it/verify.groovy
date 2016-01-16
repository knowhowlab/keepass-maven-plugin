def properties = new Properties()
properties.load(new FileReader(new File(basedir, "output.properties")))

assert properties.getProperty("nexus.test0.username").equals("khl")
assert properties.getProperty("nexus.test0.url").equals("http://nexus:8081/khl")
assert properties.getProperty("nexus.test0.password").equals("khl-oss")

assert properties.getProperty("nexus.test1.username").equals("khl")
assert properties.getProperty("nexus.test1.url").equals("http://nexus:8081/khl")
assert properties.getProperty("nexus.test1.password").equals("khl-oss")

assert properties.getProperty("nexus.test2.username").equals("khl")
assert properties.getProperty("nexus.test2.url").equals("http://nexus:8081/khl")
assert properties.getProperty("nexus.test2.password").equals("khl-oss")

assert properties.getProperty("nexus.test3.username").equals("khl")
assert properties.getProperty("nexus.test3.url").equals("http://nexus:8081/khl")
assert properties.getProperty("nexus.test3.password").equals("khl-oss")

assert properties.getProperty("nexus.test4.username").equals("khl")
assert properties.getProperty("nexus.test4.url").equals("http://nexus:8081/khl")
assert properties.getProperty("nexus.test4.password").equals("khl-oss")

assert properties.getProperty("http.test0.username").equals("admin")
assert properties.getProperty("http.test0.url").equals("http://localhost:8080")
assert properties.getProperty("http.test0.password").equals("123456")

assert properties.getProperty("http.test1.username").equals("admin")
assert properties.getProperty("http.test1.url").equals("http://localhost:8080")
assert properties.getProperty("http.test1.password").equals("123456")

assert properties.getProperty("http.test2.username").equals("admin")
assert properties.getProperty("http.test2.url").equals("http://localhost:8080")
assert properties.getProperty("http.test2.password").equals("123456")

assert properties.getProperty("http.test3.username").equals("admin")
assert properties.getProperty("http.test3.url").equals("http://localhost:8080")
assert properties.getProperty("http.test3.password").equals("123456")

assert properties.getProperty("http.test4.username").equals("admin")
assert properties.getProperty("http.test4.url").equals("http://localhost:8080")
assert properties.getProperty("http.test4.password").equals("123456")

assert properties.getProperty("http.test5.username").equals("admin")
assert properties.getProperty("http.test5.url").equals("http://localhost:8080")
assert properties.getProperty("http.test5.password").equals("123456")

assert properties.getProperty("ssh.test0.username").equals("root")
assert properties.getProperty("ssh.test0.url").equals("")
assert properties.getProperty("ssh.test0.password").equals("qwerty")
assert properties.getProperty("ssh.test0.test_attr").equals("test value")
assert properties.getProperty("ssh.test0.manager.url").equals("http://localhost:21")
