import axios from 'axios'
import {ElMessage} from "element-plus";

// axios.defaults.headers.common['token'] = localStorage.getItem('token')

// Request interceptors for API calls
axios.interceptors.request.use(
  request => {
    request.headers.token = localStorage.getItem('token');
    console.log(request.headers)
    return request;
  }, function (error) {
    // 对请求错误做些什么
    return Promise.reject(error);
  }
);
axios.interceptors.response.use(
  response => {
    console.log(response.data)
    if(response.data.code === -1){
      ElMessage.error(response.data.data)
    }
    return response
  }
)

