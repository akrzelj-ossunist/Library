export const cleanEmptyUrlParams = () => {
  const params = new URLSearchParams(window.location.search);

  let keysForDel: string[] = [];
  params.forEach((value, key) => {
    if (value == "" || value == "null") {
      keysForDel.push(key);
    }
  });

  keysForDel.forEach((key) => {
    params.delete(key);
  });

  return params.toString();
};
