import axios from "axios";
import url from "@/index";
import {reactive, ref, toRaw} from "vue";

export async function newContentApi(type : string, formData : any) {

  let responseData = reactive({
    code: -1,
    data: ''
  })

  await axios.post(url + '/content/' + type, formData,{
    headers: {
      "Content-Type": "multipart/form-data"
    }
  }).then(function (response) {
    responseData.code = response.data.code
    responseData.data = response.data.data
  }).catch(function (error) {
    console.log(error)
  })
  return responseData
}
