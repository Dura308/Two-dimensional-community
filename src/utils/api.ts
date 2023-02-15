import {reactive} from "vue";
import axios from "axios";
import url from "@/index";

/**
 * 封装post方法
 * @param suffixUrl
 * @param data
 * @param config
 * @returns {Promise}
 */
export function newPostRequest(suffixUrl : string, data : any, config = {}) {
  return new Promise((resolve, reject) => {
    axios.post(url + suffixUrl, data, config).then(response => {
      resolve(response.data);
    }).catch(err => {
      reject(err)
    })
  })
}

/**
 * 封装get方法
 * @param suffixUrl
 * @param params
 * @returns {Promise}
 */
export function newGetRequest(suffixUrl : string, params = {}) {
  return new Promise((resolve, reject) => {
    axios.get(url + suffixUrl, {
      params: params
    }).then(response => {
      resolve(response.data);
    }).catch(err => {
      reject(err)
    })
  })
}

/**
 * 封装put方法
 * @param suffixUrl
 * @param formData
 * @param config
 * @returns {Promise}
 */
export function newPutRequest(suffixUrl : string, formData : any, config = {}) {
  return new Promise((resolve, reject) => {
    axios.put(url + suffixUrl, formData, config).then(response => {
      resolve(response.data.data);
    }).catch(err => {
      reject(err)
    })
  })
}
