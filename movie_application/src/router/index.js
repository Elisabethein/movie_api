import {createRouter, createWebHistory} from 'vue-router';
import SessionsPage from '../views/SessionsPage.vue';
import ASessionPage from '../views/ASessionPage.vue';
import SignUp from '../views/SignupPage.vue';
import LogIn from '../views/LoginPage.vue';

const routes = [
    {
        path: "/",
        name: "SessionsPage",
        component: SessionsPage,
    },
    {
        path: "/session/:id",
        name: "ASessionPage",
        component: ASessionPage,
    },
    {
        path: "/signup",
        name: "SignUp",
        component: SignUp,
    },
    {
        path: "/login",
        name: "LogIn",
        component: LogIn,
    },
    {
        path: "/:catchAll(.*)",
        redirect: "/",
    },
];

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes,
});

export default router;
