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
