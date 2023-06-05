// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: AGPL-3.0-or-later

/**
 * Beacon Suedtirol API
 * The API for the Beacon Suedtirol project for configuring beacons and accessing beacon data.
 *
 * OpenAPI spec version: 0.1
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package it.bz.beacon.beaconsuedtirolsdk.swagger.client.api;

import io.swagger.client.ApiInvoker;
import io.swagger.client.ApiException;
import io.swagger.client.Pair;

import it.bz.beacon.beaconsuedtirolsdk.swagger.client.model.*;

import java.util.*;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import it.bz.beacon.beaconsuedtirolsdk.swagger.client.model.BaseMessage;
import it.bz.beacon.beaconsuedtirolsdk.swagger.client.model.BeaconImage;
import java.io.File;
import it.bz.beacon.beaconsuedtirolsdk.swagger.client.model.Resource;

import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class ImageControllerApi {
  String basePath = "https://api.beacon.bz.it";
  ApiInvoker apiInvoker = ApiInvoker.getInstance();

  public void addHeader(String key, String value) {
    getInvoker().addDefaultHeader(key, value);
  }

  public ApiInvoker getInvoker() {
    return apiInvoker;
  }

  public void setBasePath(String basePath) {
    this.basePath = basePath;
  }

  public String getBasePath() {
    return basePath;
  }

  /**
  * Create an image for a beacon
  * 
   * @param beaconId beaconId
   * @param file file
   * @return BeaconImage
  */
  public BeaconImage createUsingPOST1 (String beaconId, File file) throws TimeoutException, ExecutionException, InterruptedException, ApiException {
    Object postBody = null;
    // verify the required parameter 'beaconId' is set
    if (beaconId == null) {
      VolleyError error = new VolleyError("Missing the required parameter 'beaconId' when calling createUsingPOST1",
        new ApiException(400, "Missing the required parameter 'beaconId' when calling createUsingPOST1"));
    }
    // verify the required parameter 'file' is set
    if (file == null) {
      VolleyError error = new VolleyError("Missing the required parameter 'file' when calling createUsingPOST1",
        new ApiException(400, "Missing the required parameter 'file' when calling createUsingPOST1"));
    }

    // create path and map variables
    String path = "/v1/admin/beacons/{beaconId}/images".replaceAll("\\{" + "beaconId" + "\\}", apiInvoker.escapeString(beaconId.toString()));

    // query params
    List<Pair> queryParams = new ArrayList<Pair>();
    // header params
    Map<String, String> headerParams = new HashMap<String, String>();
    // form params
    Map<String, String> formParams = new HashMap<String, String>();
    String[] contentTypes = {
      "multipart/form-data"
    };
    String contentType = contentTypes.length > 0 ? contentTypes[0] : "application/json";

    if (contentType.startsWith("multipart/form-data")) {
      // file uploading
      MultipartEntityBuilder localVarBuilder = MultipartEntityBuilder.create();
      if (file != null) {
        localVarBuilder.addBinaryBody("file", file);
      }
      HttpEntity httpEntity = localVarBuilder.build();
      postBody = httpEntity;
    } else {
      // normal form params
    }

    String[] authNames = new String[] { "JWT" };

    try {
      String localVarResponse = apiInvoker.invokeAPI (basePath, path, "POST", queryParams, postBody, headerParams, formParams, contentType, authNames);
      if (localVarResponse != null) {
         return (BeaconImage) ApiInvoker.deserialize(localVarResponse, "", BeaconImage.class);
      } else {
         return null;
      }
    } catch (ApiException ex) {
       throw ex;
    } catch (InterruptedException ex) {
       throw ex;
    } catch (ExecutionException ex) {
      if (ex.getCause() instanceof VolleyError) {
        VolleyError volleyError = (VolleyError)ex.getCause();
        if (volleyError.networkResponse != null) {
          throw new ApiException(volleyError.networkResponse.statusCode, volleyError.getMessage());
        }
      }
      throw ex;
    } catch (TimeoutException ex) {
      throw ex;
    }
  }

      /**
   * Create an image for a beacon
   * 
   * @param beaconId beaconId   * @param file file
  */
  public void createUsingPOST1 (String beaconId, File file, final Response.Listener<BeaconImage> responseListener, final Response.ErrorListener errorListener) {
    Object postBody = null;

    // verify the required parameter 'beaconId' is set
    if (beaconId == null) {
      VolleyError error = new VolleyError("Missing the required parameter 'beaconId' when calling createUsingPOST1",
        new ApiException(400, "Missing the required parameter 'beaconId' when calling createUsingPOST1"));
    }
    // verify the required parameter 'file' is set
    if (file == null) {
      VolleyError error = new VolleyError("Missing the required parameter 'file' when calling createUsingPOST1",
        new ApiException(400, "Missing the required parameter 'file' when calling createUsingPOST1"));
    }

    // create path and map variables
    String path = "/v1/admin/beacons/{beaconId}/images".replaceAll("\\{format\\}","json").replaceAll("\\{" + "beaconId" + "\\}", apiInvoker.escapeString(beaconId.toString()));

    // query params
    List<Pair> queryParams = new ArrayList<Pair>();
    // header params
    Map<String, String> headerParams = new HashMap<String, String>();
    // form params
    Map<String, String> formParams = new HashMap<String, String>();



    String[] contentTypes = {
      "multipart/form-data"
    };
    String contentType = contentTypes.length > 0 ? contentTypes[0] : "application/json";

    if (contentType.startsWith("multipart/form-data")) {
      // file uploading
      MultipartEntityBuilder localVarBuilder = MultipartEntityBuilder.create();
      
      if (file != null) {
        localVarBuilder.addBinaryBody("file", file);
      }
      

      HttpEntity httpEntity = localVarBuilder.build();
      postBody = httpEntity;
    } else {
      // normal form params
      
    }

    String[] authNames = new String[] { "JWT" };

    try {
      apiInvoker.invokeAPI(basePath, path, "POST", queryParams, postBody, headerParams, formParams, contentType, authNames,
        new Response.Listener<String>() {
          @Override
          public void onResponse(String localVarResponse) {
            try {
              responseListener.onResponse((BeaconImage) ApiInvoker.deserialize(localVarResponse,  "", BeaconImage.class));
            } catch (ApiException exception) {
               errorListener.onErrorResponse(new VolleyError(exception));
            }
          }
      }, new Response.ErrorListener() {
          @Override
          public void onErrorResponse(VolleyError error) {
            errorListener.onErrorResponse(error);
          }
      });
    } catch (ApiException ex) {
      errorListener.onErrorResponse(new VolleyError(ex));
    }
  }
  /**
  * Delete an image
  * 
   * @param beaconId beaconId
   * @param id id
   * @return BaseMessage
  */
  public BaseMessage deleteUsingDELETE (String beaconId, Long id) throws TimeoutException, ExecutionException, InterruptedException, ApiException {
    Object postBody = null;
    // verify the required parameter 'beaconId' is set
    if (beaconId == null) {
      VolleyError error = new VolleyError("Missing the required parameter 'beaconId' when calling deleteUsingDELETE",
        new ApiException(400, "Missing the required parameter 'beaconId' when calling deleteUsingDELETE"));
    }
    // verify the required parameter 'id' is set
    if (id == null) {
      VolleyError error = new VolleyError("Missing the required parameter 'id' when calling deleteUsingDELETE",
        new ApiException(400, "Missing the required parameter 'id' when calling deleteUsingDELETE"));
    }

    // create path and map variables
    String path = "/v1/admin/beacons/{beaconId}/images/{id}".replaceAll("\\{" + "beaconId" + "\\}", apiInvoker.escapeString(beaconId.toString())).replaceAll("\\{" + "id" + "\\}", apiInvoker.escapeString(id.toString()));

    // query params
    List<Pair> queryParams = new ArrayList<Pair>();
    // header params
    Map<String, String> headerParams = new HashMap<String, String>();
    // form params
    Map<String, String> formParams = new HashMap<String, String>();
    String[] contentTypes = {
    };
    String contentType = contentTypes.length > 0 ? contentTypes[0] : "application/json";

    if (contentType.startsWith("multipart/form-data")) {
      // file uploading
      MultipartEntityBuilder localVarBuilder = MultipartEntityBuilder.create();
      HttpEntity httpEntity = localVarBuilder.build();
      postBody = httpEntity;
    } else {
      // normal form params
    }

    String[] authNames = new String[] { "JWT" };

    try {
      String localVarResponse = apiInvoker.invokeAPI (basePath, path, "DELETE", queryParams, postBody, headerParams, formParams, contentType, authNames);
      if (localVarResponse != null) {
         return (BaseMessage) ApiInvoker.deserialize(localVarResponse, "", BaseMessage.class);
      } else {
         return null;
      }
    } catch (ApiException ex) {
       throw ex;
    } catch (InterruptedException ex) {
       throw ex;
    } catch (ExecutionException ex) {
      if (ex.getCause() instanceof VolleyError) {
        VolleyError volleyError = (VolleyError)ex.getCause();
        if (volleyError.networkResponse != null) {
          throw new ApiException(volleyError.networkResponse.statusCode, volleyError.getMessage());
        }
      }
      throw ex;
    } catch (TimeoutException ex) {
      throw ex;
    }
  }

      /**
   * Delete an image
   * 
   * @param beaconId beaconId   * @param id id
  */
  public void deleteUsingDELETE (String beaconId, Long id, final Response.Listener<BaseMessage> responseListener, final Response.ErrorListener errorListener) {
    Object postBody = null;

    // verify the required parameter 'beaconId' is set
    if (beaconId == null) {
      VolleyError error = new VolleyError("Missing the required parameter 'beaconId' when calling deleteUsingDELETE",
        new ApiException(400, "Missing the required parameter 'beaconId' when calling deleteUsingDELETE"));
    }
    // verify the required parameter 'id' is set
    if (id == null) {
      VolleyError error = new VolleyError("Missing the required parameter 'id' when calling deleteUsingDELETE",
        new ApiException(400, "Missing the required parameter 'id' when calling deleteUsingDELETE"));
    }

    // create path and map variables
    String path = "/v1/admin/beacons/{beaconId}/images/{id}".replaceAll("\\{format\\}","json").replaceAll("\\{" + "beaconId" + "\\}", apiInvoker.escapeString(beaconId.toString())).replaceAll("\\{" + "id" + "\\}", apiInvoker.escapeString(id.toString()));

    // query params
    List<Pair> queryParams = new ArrayList<Pair>();
    // header params
    Map<String, String> headerParams = new HashMap<String, String>();
    // form params
    Map<String, String> formParams = new HashMap<String, String>();



    String[] contentTypes = {
      
    };
    String contentType = contentTypes.length > 0 ? contentTypes[0] : "application/json";

    if (contentType.startsWith("multipart/form-data")) {
      // file uploading
      MultipartEntityBuilder localVarBuilder = MultipartEntityBuilder.create();
      

      HttpEntity httpEntity = localVarBuilder.build();
      postBody = httpEntity;
    } else {
      // normal form params
          }

    String[] authNames = new String[] { "JWT" };

    try {
      apiInvoker.invokeAPI(basePath, path, "DELETE", queryParams, postBody, headerParams, formParams, contentType, authNames,
        new Response.Listener<String>() {
          @Override
          public void onResponse(String localVarResponse) {
            try {
              responseListener.onResponse((BaseMessage) ApiInvoker.deserialize(localVarResponse,  "", BaseMessage.class));
            } catch (ApiException exception) {
               errorListener.onErrorResponse(new VolleyError(exception));
            }
          }
      }, new Response.ErrorListener() {
          @Override
          public void onErrorResponse(VolleyError error) {
            errorListener.onErrorResponse(error);
          }
      });
    } catch (ApiException ex) {
      errorListener.onErrorResponse(new VolleyError(ex));
    }
  }
  /**
  * View a list of available images for a beacon
  * 
   * @param beaconId beaconId
   * @return List<BeaconImage>
  */
  public List<BeaconImage> getListUsingGET1 (String beaconId) throws TimeoutException, ExecutionException, InterruptedException, ApiException {
    Object postBody = null;
    // verify the required parameter 'beaconId' is set
    if (beaconId == null) {
      VolleyError error = new VolleyError("Missing the required parameter 'beaconId' when calling getListUsingGET1",
        new ApiException(400, "Missing the required parameter 'beaconId' when calling getListUsingGET1"));
    }

    // create path and map variables
    String path = "/v1/admin/beacons/{beaconId}/images".replaceAll("\\{" + "beaconId" + "\\}", apiInvoker.escapeString(beaconId.toString()));

    // query params
    List<Pair> queryParams = new ArrayList<Pair>();
    // header params
    Map<String, String> headerParams = new HashMap<String, String>();
    // form params
    Map<String, String> formParams = new HashMap<String, String>();
    String[] contentTypes = {
    };
    String contentType = contentTypes.length > 0 ? contentTypes[0] : "application/json";

    if (contentType.startsWith("multipart/form-data")) {
      // file uploading
      MultipartEntityBuilder localVarBuilder = MultipartEntityBuilder.create();
      HttpEntity httpEntity = localVarBuilder.build();
      postBody = httpEntity;
    } else {
      // normal form params
    }

    String[] authNames = new String[] { "JWT" };

    try {
      String localVarResponse = apiInvoker.invokeAPI (basePath, path, "GET", queryParams, postBody, headerParams, formParams, contentType, authNames);
      if (localVarResponse != null) {
         return (List<BeaconImage>) ApiInvoker.deserialize(localVarResponse, "array", BeaconImage.class);
      } else {
         return null;
      }
    } catch (ApiException ex) {
       throw ex;
    } catch (InterruptedException ex) {
       throw ex;
    } catch (ExecutionException ex) {
      if (ex.getCause() instanceof VolleyError) {
        VolleyError volleyError = (VolleyError)ex.getCause();
        if (volleyError.networkResponse != null) {
          throw new ApiException(volleyError.networkResponse.statusCode, volleyError.getMessage());
        }
      }
      throw ex;
    } catch (TimeoutException ex) {
      throw ex;
    }
  }

      /**
   * View a list of available images for a beacon
   * 
   * @param beaconId beaconId
  */
  public void getListUsingGET1 (String beaconId, final Response.Listener<List<BeaconImage>> responseListener, final Response.ErrorListener errorListener) {
    Object postBody = null;

    // verify the required parameter 'beaconId' is set
    if (beaconId == null) {
      VolleyError error = new VolleyError("Missing the required parameter 'beaconId' when calling getListUsingGET1",
        new ApiException(400, "Missing the required parameter 'beaconId' when calling getListUsingGET1"));
    }

    // create path and map variables
    String path = "/v1/admin/beacons/{beaconId}/images".replaceAll("\\{format\\}","json").replaceAll("\\{" + "beaconId" + "\\}", apiInvoker.escapeString(beaconId.toString()));

    // query params
    List<Pair> queryParams = new ArrayList<Pair>();
    // header params
    Map<String, String> headerParams = new HashMap<String, String>();
    // form params
    Map<String, String> formParams = new HashMap<String, String>();



    String[] contentTypes = {
      
    };
    String contentType = contentTypes.length > 0 ? contentTypes[0] : "application/json";

    if (contentType.startsWith("multipart/form-data")) {
      // file uploading
      MultipartEntityBuilder localVarBuilder = MultipartEntityBuilder.create();
      

      HttpEntity httpEntity = localVarBuilder.build();
      postBody = httpEntity;
    } else {
      // normal form params
          }

    String[] authNames = new String[] { "JWT" };

    try {
      apiInvoker.invokeAPI(basePath, path, "GET", queryParams, postBody, headerParams, formParams, contentType, authNames,
        new Response.Listener<String>() {
          @Override
          public void onResponse(String localVarResponse) {
            try {
              responseListener.onResponse((List<BeaconImage>) ApiInvoker.deserialize(localVarResponse,  "array", BeaconImage.class));
            } catch (ApiException exception) {
               errorListener.onErrorResponse(new VolleyError(exception));
            }
          }
      }, new Response.ErrorListener() {
          @Override
          public void onErrorResponse(VolleyError error) {
            errorListener.onErrorResponse(error);
          }
      });
    } catch (ApiException ex) {
      errorListener.onErrorResponse(new VolleyError(ex));
    }
  }
  /**
  * Get an image for a beacon
  * 
   * @param beaconId beaconId
   * @param id id
   * @return Resource
  */
  public Resource serveFileUsingGET (String beaconId, Long id) throws TimeoutException, ExecutionException, InterruptedException, ApiException {
    Object postBody = null;
    // verify the required parameter 'beaconId' is set
    if (beaconId == null) {
      VolleyError error = new VolleyError("Missing the required parameter 'beaconId' when calling serveFileUsingGET",
        new ApiException(400, "Missing the required parameter 'beaconId' when calling serveFileUsingGET"));
    }
    // verify the required parameter 'id' is set
    if (id == null) {
      VolleyError error = new VolleyError("Missing the required parameter 'id' when calling serveFileUsingGET",
        new ApiException(400, "Missing the required parameter 'id' when calling serveFileUsingGET"));
    }

    // create path and map variables
    String path = "/v1/admin/beacons/{beaconId}/images/{id}".replaceAll("\\{" + "beaconId" + "\\}", apiInvoker.escapeString(beaconId.toString())).replaceAll("\\{" + "id" + "\\}", apiInvoker.escapeString(id.toString()));

    // query params
    List<Pair> queryParams = new ArrayList<Pair>();
    // header params
    Map<String, String> headerParams = new HashMap<String, String>();
    // form params
    Map<String, String> formParams = new HashMap<String, String>();
    String[] contentTypes = {
    };
    String contentType = contentTypes.length > 0 ? contentTypes[0] : "application/json";

    if (contentType.startsWith("multipart/form-data")) {
      // file uploading
      MultipartEntityBuilder localVarBuilder = MultipartEntityBuilder.create();
      HttpEntity httpEntity = localVarBuilder.build();
      postBody = httpEntity;
    } else {
      // normal form params
    }

    String[] authNames = new String[] { "JWT" };

    try {
      String localVarResponse = apiInvoker.invokeAPI (basePath, path, "GET", queryParams, postBody, headerParams, formParams, contentType, authNames);
      if (localVarResponse != null) {
         return (Resource) ApiInvoker.deserialize(localVarResponse, "", Resource.class);
      } else {
         return null;
      }
    } catch (ApiException ex) {
       throw ex;
    } catch (InterruptedException ex) {
       throw ex;
    } catch (ExecutionException ex) {
      if (ex.getCause() instanceof VolleyError) {
        VolleyError volleyError = (VolleyError)ex.getCause();
        if (volleyError.networkResponse != null) {
          throw new ApiException(volleyError.networkResponse.statusCode, volleyError.getMessage());
        }
      }
      throw ex;
    } catch (TimeoutException ex) {
      throw ex;
    }
  }

      /**
   * Get an image for a beacon
   * 
   * @param beaconId beaconId   * @param id id
  */
  public void serveFileUsingGET (String beaconId, Long id, final Response.Listener<Resource> responseListener, final Response.ErrorListener errorListener) {
    Object postBody = null;

    // verify the required parameter 'beaconId' is set
    if (beaconId == null) {
      VolleyError error = new VolleyError("Missing the required parameter 'beaconId' when calling serveFileUsingGET",
        new ApiException(400, "Missing the required parameter 'beaconId' when calling serveFileUsingGET"));
    }
    // verify the required parameter 'id' is set
    if (id == null) {
      VolleyError error = new VolleyError("Missing the required parameter 'id' when calling serveFileUsingGET",
        new ApiException(400, "Missing the required parameter 'id' when calling serveFileUsingGET"));
    }

    // create path and map variables
    String path = "/v1/admin/beacons/{beaconId}/images/{id}".replaceAll("\\{format\\}","json").replaceAll("\\{" + "beaconId" + "\\}", apiInvoker.escapeString(beaconId.toString())).replaceAll("\\{" + "id" + "\\}", apiInvoker.escapeString(id.toString()));

    // query params
    List<Pair> queryParams = new ArrayList<Pair>();
    // header params
    Map<String, String> headerParams = new HashMap<String, String>();
    // form params
    Map<String, String> formParams = new HashMap<String, String>();



    String[] contentTypes = {
      
    };
    String contentType = contentTypes.length > 0 ? contentTypes[0] : "application/json";

    if (contentType.startsWith("multipart/form-data")) {
      // file uploading
      MultipartEntityBuilder localVarBuilder = MultipartEntityBuilder.create();
      

      HttpEntity httpEntity = localVarBuilder.build();
      postBody = httpEntity;
    } else {
      // normal form params
          }

    String[] authNames = new String[] { "JWT" };

    try {
      apiInvoker.invokeAPI(basePath, path, "GET", queryParams, postBody, headerParams, formParams, contentType, authNames,
        new Response.Listener<String>() {
          @Override
          public void onResponse(String localVarResponse) {
            try {
              responseListener.onResponse((Resource) ApiInvoker.deserialize(localVarResponse,  "", Resource.class));
            } catch (ApiException exception) {
               errorListener.onErrorResponse(new VolleyError(exception));
            }
          }
      }, new Response.ErrorListener() {
          @Override
          public void onErrorResponse(VolleyError error) {
            errorListener.onErrorResponse(error);
          }
      });
    } catch (ApiException ex) {
      errorListener.onErrorResponse(new VolleyError(ex));
    }
  }
}
