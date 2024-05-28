import {createRouter, createWebHistory} from 'vue-router'
import SolutionView from "@/views/TaskView.vue";
import TasksView from "@/views/TasksView.vue";
import MainView from "@/views/MainView.vue";
import CreateTaskView from "@/views/CreateTaskView.vue";

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/task/:id',
            name: 'solution',
            component: SolutionView
        },
        {
            path: '/task',
            name: 'tasks',
            component: TasksView
        },
        {
          path: '/task/create',
          name: 'createTask',
          component: CreateTaskView
        },
        {
            path: '/',
            name: 'main',
            component: MainView
        }
    ]
})

export default router
