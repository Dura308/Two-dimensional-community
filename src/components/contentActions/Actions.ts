import {newPutRequest} from "@/utils/api";

export const like = (isLike : boolean, formData : any) => {
  if(isLike){
    newPutRequest('/content/like', formData, {
      headers: {
        "Content-Type": "multipart/form-data"
      }
    })
  }else {
    newPutRequest('/content/disLike', formData, {
      headers: {
        "Content-Type": "multipart/form-data"
      }
    })
  }
}

export const collect = (isCollect : boolean, formData : any) => {
  if(isCollect){
    newPutRequest('/content/collect', formData, {
      headers: {
        "Content-Type": "multipart/form-data"
      }
    })
  }else {
    newPutRequest('/content/cancelCollect', formData, {
      headers: {
        "Content-Type": "multipart/form-data"
      }
    })
  }
}
