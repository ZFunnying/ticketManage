
import AWS from 'aws-sdk';
var s3Client = new AWS.S3({
  endpoint: 'http://192.168.50.100:9000/',
  accessKeyId: 'admin',
  secretAccessKey: '12345678',
  s3BucketEndpoint: true,
  s3ForcePathStyle: true,
  signatureVersion: 'v4',
  logger: window.console,
})
export default s3Client
