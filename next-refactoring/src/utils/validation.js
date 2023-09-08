/**
 * 게시글 유효성 검증을 수행
 * @param formData
 * @returns {boolean}
 */
export function validateFormData(formData) {

    let errors = []; // 에러 리스트

    const titleRegex = /^[\s\S]{4,1999}$/;  // 4자 이상 100자 미만
    const contentRegex = /^[\s\S]{4,1999}$/;  // 4자 이상 2000자 미만
    const formDataEntries = Array.from(formData.entries()); // formData 내의 key-value를 리스트화

    formDataEntries.forEach(([key, value]) => {
        if (key === 'title') {
            const isTitleValid = titleRegex.test(value.toString());
            if (!isTitleValid) {
                errors.push('게시글 제목은 4자 이상 100자 미만이어야 합니다.');
            }

            // TODO: else if 말고 if문을 개별로 사용
        } else if (key === 'content') {
            const isContentValid = contentRegex.test(value.toString());
            if (!isContentValid) {
                errors.push('게시글 내용은 4자 이상 2000자 이하여야 합니다.');
            }
        } else if (key === 'categoryId') {
            if (value === null || value === '') {
                errors.push('카테고리를 선택해야 합니다.')
            }
        } else if (key === 'target') {
            if (value === null || value === '') {
                errors.push('노출 대상 게시판을 지정해야 합니다.')
            }
        } else if (key === 'secret') {
            if (value === null || value === '') {
                errors.push('비밀 게시글 지정 여부를 선택해야 합니다.')
            }
        }
    });


    if (errors.length > 0) {
        alert(errors.join('\n'));
        return false; // 유효성 검증 실패
    }

    return true; // 유효성 검증 성공
}