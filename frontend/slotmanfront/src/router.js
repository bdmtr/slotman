import { createRouter, createWebHistory } from 'vue-router';

const routes = [
    {
        path: '/',
        name: 'Home',
        component: () => import('@/components/Home.vue'),
    },
    {
        path: '/transactions',
        name: 'Transactions',
        component: () => import('@/components/Transaction.vue'),
    },
];

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes,
});

export default router;