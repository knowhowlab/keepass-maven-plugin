def properties = new Properties()
properties.load(new FileReader(new File(basedir, "output.properties")))

assert properties.getProperty("ssh.username").equals("root")
assert properties.getProperty("ssh.url").equals("127.0.0.1")
assert properties.getProperty("ssh.password").equals("test")

assert properties.getProperty("http.username").equals("admin")
assert properties.getProperty("http.url").equals("http://localhost:8080")
assert properties.getProperty("http.password").equals("admin")
