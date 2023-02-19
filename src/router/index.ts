import { createRouter, createWebHashHistory, RouteRecordRaw } from 'vue-router'
import homeView from '../views/home/Home.vue'
import newPictureView from '../views/newContent/NewPicture.vue'
import newTextView from '../views/newContent/NewText.vue'
import newQuestionView from '../views/newContent/NewQuestion.vue'
// @ts-ignore
import newVideoView from '../views/newContent/NewVideo.vue'
import personalCenterView from '../views/personal/PersonalCenter.vue'


const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    redirect: '/home'
  },
  {
    path: '/home',
    name: 'home',
    component: homeView
  },
  {
    path: '/newPicture',
    name: 'newPicture',
    component: newPictureView
  },
  {
    path: '/newText',
    name: 'newText',
    component: newTextView
  },
  {
    path: '/newQuestion',
    name: 'newQuestion',
    component: newQuestionView
  },
  {
    path: '/newVideo',
    name: 'newVideo',
    component: newVideoView
  },
  {
    path: '/personalCenter',
    name: 'personalCenter',
    component: personalCenterView
  }

]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

export default router
