import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import App from './App';
import { AppToaster } from './shared/toast/AppToaster';
import './styles/global.css';

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <App />
    <AppToaster />
  </StrictMode>,
);

