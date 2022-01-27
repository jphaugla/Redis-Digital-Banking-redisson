keytool -genkey \
  -dname "cn=CLIENT_APP_01" \
  -alias truststorekey \
  -keyalg RSA \
  -keystore ./client-truststore.p12 \
  -keypass somesillypw \
  -storepass somesillypw \
  -storetype pkcs12
