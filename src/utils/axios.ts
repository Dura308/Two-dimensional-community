import axios from 'axios'
import {ElMessage} from "element-plus";
import store from '../store';

// Request interceptors for API calls
axios.interceptors.request.use(
  request => {
    request.headers.token = localStorage.getItem('token');
    return request;
  }, function (error) {
    // 对请求错误做些什么
    return Promise.reject(error);
  }
);

axios.interceptors.response.use(
  response => {
    if(response.data.code === -10000){
      ElMessage({
        message: response.data,
        grouping: true,
        type: 'error',
      })
      store.commit('removeToken')
      localStorage.removeItem('store')
      localStorage.removeItem('token')
      setTimeout(() => {
        location.reload()
      },1000)
      return response
    }

    let renewToken = response.headers["renew-token"];
    if (renewToken != undefined){
      store.commit('parseToken', renewToken)
      localStorage.setItem('token', renewToken)
      console.log(store.state.token)
    }
    return response
  }
)

