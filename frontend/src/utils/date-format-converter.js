/**
 * 게시글 날짜 포맷 변환
 * @param list 변환할 게시글 리스트
 * @returns {*}
 */
const convertDateFormat = (list) => {
    list.forEach(obj => {
        // createdDate 변환
        if (obj.createdDate) {
            const dateParts = obj.createdDate.split(" ");
            obj.createdDate = dateParts[0] + " " + dateParts[1].split(":").slice(0, 2).join(":");
        }

        // modifiedDate 변환
        if (obj.modifiedDate) {
            const dateParts = obj.modifiedDate.split(" ");
            obj.modifiedDate = dateParts[0] + " " + dateParts[1].split(":").slice(0, 2).join(":");
        }
    });

    return list
}


export { convertDateFormat }