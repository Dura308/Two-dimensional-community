import axios from 'axios'
import {ElMessage} from "element-plus";
import store from '../store';



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
    let renewToken = response.headers["renew-token"];
    console.log(renewToken)
    if (renewToken != undefined){
      store.commit('setToken', renewToken)
      localStorage.setItem('token', renewToken)
      console.log(store.state.token)
    }
    return response
  }
)

