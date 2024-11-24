import { getCLS, getFID, getFCP, getLCP, getTTFB,Metric } from 'web-vitals';

type ReportHandler = (metric: Metric) => void;

const reportWebVitals = (onPerfEntry?: ReportHandler): void => {
  if (onPerfEntry && typeof onPerfEntry === 'function') {
    getCLS(onPerfEntry); // CLS - Cumulative Layout Shift
    getFID(onPerfEntry); // FID - First Input Delay
    getFCP(onPerfEntry); // FCP - First Contentful Paint
    getLCP(onPerfEntry); // LCP - Largest Contentful Paint
    getTTFB(onPerfEntry); // TTFB - Time to First Byte
  }
};

export default reportWebVitals;
