import { createRouter, createWebHistory } from 'vue-router';

const routes = [
    {
        path: '/',
        name: 'Home',
        component: () => import('@/components/Login.vue'),
    },
    {
        path: '/transactions',
        name: 'Transactions',
        component: () => import('@/components/Transaction.vue'),
    },
    {
        path: '/personal',
        name: 'Personal',
        component: () => import('@/components/Personal.vue'),
    },
    {
        path: '/register',
        name: 'Register',
        component: () => import('@/components/Register.vue'),
    },
    {
        path: '/login',
        name: 'Login',
        component: () => import('@/components/Login.vue'),
    },
];

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes,
});

export default router;