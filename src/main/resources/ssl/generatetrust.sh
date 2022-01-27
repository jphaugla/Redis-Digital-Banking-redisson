keytool -genkey \
  -dname "cn=CLIENT_APP_01" \
  -alias truststorekey \
  -keyalg RSA \
  -keystore ./client-truststore.p12 \
  -keypass jasonrocks \
  -storepass jasonrocks \
  -storetype pkcs12
