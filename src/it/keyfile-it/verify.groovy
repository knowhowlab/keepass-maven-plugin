def properties = new Properties()
properties.load(new FileReader(new File(basedir, "output.properties")))

assert properties.getProperty("test.username").equals("test")
assert properties.getProperty("test.url").equals("none")
assert properties.getProperty("test.password").equals("pass")
