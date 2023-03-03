import { createWebHistory, createRouter } from "vue-router";
import ErrorPage from "@/components/ErrorPage.vue";
import Board from "@/components/Board.vue";
import MainPage from "@/components/MainPage.vue";
import View from "@/components/View.vue";

const router = createRouter({
    history : createWebHistory(),
    routes : [ // path별 component를 추가한다.
        { path : "/", name : "mainPage", component : MainPage},
        { path : "/boards/list/:page?", name : "board", component : Board },
        { path : "/boards/view/:postId", name : "view", component : View },
        {
            path : "/:pathMatch(.*)",
            name : "not-found",
            component : ErrorPage
        }
    ]
});

export default router;