# 让S3存储桶中的文件所有人可以访问

## 生成策略

https://awspolicygen.s3.amazonaws.com/policygen.html

![生成策略](./images/生成策略.png)

## 编辑存储桶策略

![编辑存储桶策略](./images/编辑存储桶策略.png)

## 公共访问权限

![公共访问权限](./images/公共访问权限.png)



# S3 SDK 文件上传

## 实现

```java
@SpringBootTest
class AwsStudyApplicationTests {

    private static final String ACCESS_KEY_ID = "xxxxxx";
    private static final String SECRET_KEY_ID = "yyyyyyyyyyyyyyy";
    private static final String REGION = "ap-northeast-1";

    private static final String BUCKET_NAME = "bucket-0704-test1";

    private static AmazonS3 s3Client;

    @BeforeAll
    static void setup() {
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(ACCESS_KEY_ID, SECRET_KEY_ID);
        s3Client = AmazonS3Client.builder().withRegion(REGION).withCredentials(new AWSStaticCredentialsProvider(awsCredentials)).build();
    }

    // 获取所有的存储桶列表
    @Test
    void testListBuckets() {
        List<Bucket> buckets = s3Client.listBuckets();
        for (Bucket bucket : buckets) {
            System.out.println(bucket.getName());
        }
    }

    // 文件上传
    @Test
    void testUpload(){
        s3Client.putObject(BUCKET_NAME, "test.jpg", new File("angle.jpg"));
    }
}
```

## IAM

## 安全凭证

![安全凭证](./images/安全凭证.png)

![IAM](./images/IAM.png)

## 添加用户

![添加用户](./images/添加用户.png)

## 添加权限

选中直接附加策略，搜索 AmazonS3FullAccess

![添加权限](./images/添加权限.png)

## 访问密钥

创建完成用户之后，在安全凭证中创建访问密钥

![访问密钥](./images/创建访问密钥.png)

![获取访问密钥](./images/获取访问密钥.png)

