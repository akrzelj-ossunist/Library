/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {},
    screens: {
      'phone': {"max": '375px'},
  
      'tablet': {"max": '850px'},
  
      'desktop': {"min": '851px'},
    },
  },
  plugins: [],
}