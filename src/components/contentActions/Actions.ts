import {newPutRequest} from "@/utils/api";

export const likeOperation = (formData : any) => {
  return new Promise(resolve => {
    newPutRequest('/content/likeOperation', formData, {
      headers: {
        "Content-Type": "multipart/form-data"
      }
    }).then((response:any) => {
      if (response.code === 200){
        resolve(true)
      }
    })
  })
}

export const collectOperation = (formData : any) => {
  return new Promise(resolve => {
    newPutRequest('/content/collectOperation', formData, {
      headers: {
        "Content-Type": "multipart/form-data"
      }
    }).then((response:any) => {
      if (response.code === 200){
        resolve(true)
      }
    })
  })
}
