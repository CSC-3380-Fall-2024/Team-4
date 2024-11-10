"use strict";
/* tutorial referenced: https://youtu.be/D8fdAsIxDh8?si=2R3RB-d9MX9b1--O */
var __createBinding = (this && this.__createBinding) || (Object.create ? (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    var desc = Object.getOwnPropertyDescriptor(m, k);
    if (!desc || ("get" in desc ? !m.__esModule : desc.writable || desc.configurable)) {
      desc = { enumerable: true, get: function() { return m[k]; } };
    }
    Object.defineProperty(o, k2, desc);
}) : (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    o[k2] = m[k];
})); 
var __setModuleDefault = (this && this.__setModuleDefault) || (Object.create ? (function(o, v) {
    Object.defineProperty(o, "default", { enumerable: true, value: v });
}) : function(o, v) {
    o["default"] = v;
});
var __importStar = (this && this.__importStar) || function (mod) {
    if (mod && mod.__esModule) return mod;
    var result = {};
    if (mod != null) for (var k in mod) if (k !== "default" && Object.prototype.hasOwnProperty.call(mod, k)) __createBinding(result, mod, k);
    __setModuleDefault(result, mod);
    return result;
};
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
/* Importing React library to use JSX syntax and create components */
var react_1 = __importDefault(require("react"));
/* Importing CSS styles from Nav.module.css for styling the navigation bar */
var Nav_module_css_1 = __importDefault(require("./Nav.module.css"));
/* Importing data from links.json file which contains the navigation links */
var data = __importStar(require("./links.json"));
/* If using the public folder, we reference the image directly */
var logo = '/logo.png';  // Using path from public folder

/* Retrieve the 'links' array from the JSON data
* Convert the imported data into a string and then parse it back into an object */
var linksString = JSON.stringify(data);
var links = JSON.parse(linksString).links;
/* Functional component to render the list of links */
var Links = function (_a) {
    var links = _a.links;
    /* Returning a div containing each link rendered as a clickable item */
    return (react_1.default.createElement("div", { className: Nav_module_css_1.default['links-container'] }, links.map(function (link) {
        /* Each link is displayed in a div with a unique key (href) */
        return (react_1.default.createElement("div", { key: link.href, className: Nav_module_css_1.default['link'] },
            react_1.default.createElement("a", { href: link.href },
                link.label,
                " ")));
    })));
};
/* Main functional component for the nav bar*/
var Nav = function () {
    /* Render the navigation bar with a logo and links */
    return (react_1.default.createElement("nav", { className: Nav_module_css_1.default.navbar },
        react_1.default.createElement("div", { className: Nav_module_css_1.default['logo-container'] },
            react_1.default.createElement("img", { src: logo, alt: "logo", className: Nav_module_css_1.default.logo }) /* Use logo image here */),
        react_1.default.createElement(Links, { links: links })));
};
/* Exporting the Nav component for use in other parts of the application */
exports.default = Nav;
