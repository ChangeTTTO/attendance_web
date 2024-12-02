import container from "../page/container.vue";
import {createRouter, createWebHistory,createWebHashHistory} from "vue-router";
import student from "../page/student.vue";

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/',
            component: container,
            redirect: '/welcome',
           /* children: [
                {
                    name:'学生管理界面',
                    path: '/student',
                    component: student,
                }
            ]*/

        },
        {
            name: '学生管理界面',
            path: '/student',
            component: student,
        }

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