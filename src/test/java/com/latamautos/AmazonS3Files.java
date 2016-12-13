package com.latamautos;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.AnonymousAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Harold on 1/12/16.
 */
public class AmazonS3Files {

    public List<String> listfiles(String microserviceName){
        AnonymousAWSCredentials awsCreds = new AnonymousAWSCredentials();
        AmazonS3 s3client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .withRegion(Regions.US_EAST_1)
                .build();

        try {
            System.out.println("Listing objects");
            List<String> urlList = new ArrayList<>();
            String existingBucketName = "microservice-pacts";
            final ListObjectsV2Request req = new ListObjectsV2Request().withBucketName(existingBucketName).withPrefix(microserviceName + "/stage").withMaxKeys(2);
            ListObjectsV2Result result;
            do {
                result = s3client.listObjectsV2(req);

                for (S3ObjectSummary objectSummary :
                        result.getObjectSummaries()) {
                    System.out.println(" - " + objectSummary.getKey() + "  " +
                            "(size = " + objectSummary.getSize() +
                            ")");
                    System.out.println("url amazon s3 ----->>>>>>>>>> " + "https://s3.amazonaws.com/microservice-pacts/" + objectSummary.getKey());
                    urlList.add("https://s3.amazonaws.com/microservice-pacts/" + objectSummary.getKey());
                }
                System.out.println("Next Continuation Token : " + result.getNextContinuationToken());
                req.setContinuationToken(result.getNextContinuationToken());
            } while(result.isTruncated());
            return urlList;
        } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, " +
                    "which means your request made it " +
                    "to Amazon S3, but was rejected with an error response " +
                    "for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, " +
                    "which means the client encountered " +
                    "an internal error while trying to communicate" +
                    " with S3, " +
                    "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
        }
        return new ArrayList<>();
    }

}
