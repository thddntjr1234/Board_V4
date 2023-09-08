import '../src/styles/index.scss';

import '@/styles/globals.css'

if (typeof window !== 'undefined') {
  require('jquery/dist/jquery.slim');
  require('bootstrap/dist/js/bootstrap.bundle');
}

export default function App({ Component, pageProps }) {
  return <Component {...pageProps} />
}
