  /**
   * localStorage에서 jwt토큰이 존재하는지를 확인
   * @returns {boolean}
   */
  const checkValidToken = () => {
    return !!localStorage.getItem("token");
  }

  export {checkValidToken}