module.exports = {
      setupFilesAfterEnv: ['<rootDir>/src/setupTests.ts'], 
      testEnvironment: 'jsdom',
      transform: {
      '^.+\\.tsx?$': 'ts-jest',
    },
      testMatch: ['**/?(*.)+(spec|test).ts?(x)'],
  
    collectCoverage: true,
    coverageDirectory: 'coverage',
    coverageProvider: 'v8',
    transformIgnorePatterns: ['node_modules/'],
  };
  