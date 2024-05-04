import {axiosApi} from "@/api/api";

export default {

    sendAttempt(data) {
        return axiosApi.post(
            "/solutions/task/" + data.taskId,
            data, {
                headers: { 'x-user-id': '803f29aa-cca4-4fe7-8254-e0005106cdc3'}
        }
        )
    },
    getAttemptById(id) {
        return axiosApi.get(
            "/solutions/" + id,
        )
    }
}