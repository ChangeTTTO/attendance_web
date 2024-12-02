import container from "../page/container.vue";
import {createRouter, createWebHistory} from "vue-router";

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/',
            component: container,
            redirect: '/welcome',
            /*children: [

            ]*/
        },

       /* {
            path: '/login',
            component: login
        },
        {
            path: '/register',
            component: register
        }*/
    ]
})

export default router