export const concatArrayOfArrays = (arr: any[]): any[] => {
    let newArray: any[] = []
    arr.map(el => newArray = newArray.concat(el))
    return newArray
  }