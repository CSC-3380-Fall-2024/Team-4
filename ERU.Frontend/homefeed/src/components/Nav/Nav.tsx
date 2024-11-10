/* tutorial referenced: https://youtu.be/D8fdAsIxDh8?si=2R3RB-d9MX9b1--O */

/* Importing React library to use JSX syntax and create components */
import React from 'react';

import { Link } from 'react-router-dom';

/* Importing CSS styles from Nav.module.css for styling the navigation bar */
import styles from './Nav.module.css';

/* Importing data from links.json file which contains the navigation links */
import * as data from './links.json';
import logo from '../assets/logo.png';




/* Retrieve the 'links' array from the JSON data
* Convert the imported data into a string and then parse it back into an object */
const linksString = JSON.stringify(data);
const links = JSON.parse(linksString).links;


/* Define a type (string) for each link, which has a label and href
* label (string) = the text displayed for the link
* href (string) = the URL the link points to */
type Link = {
    label: string;
    href: string;
}

/* Functional component to render the list of links */
const Links: React.FC<{ links: Link[] }> = function({ links }) {
    /* Returning a div containing each link rendered as a clickable item */
    return (
        <div className={styles['links-container']}>
            {links.map(function(link: Link) {
                /* Each link is displayed in a div with a unique key (href) */
                return (
                    <div key={link.href} className={styles['link']}>
                        <Link to={link.href}>
                            {link.label} {/* Displaying the label for the link */}
                        </Link>
                    </div>
                );
            })}
        </div>
    );
}

/* Main functional component for the nav bar*/
const Nav: React.FC = function() {
    /* Render the navigation bar with a logo and links */
    return (
        <nav className={styles.navbar}>
            <div className={styles['logo-container']}>
            <img src={logo} alt="logo" className={styles.logo} /> {/* Logo image */}
            </div>
            <Links links={links} /> {/* Render the Links component with the links data */}
        </nav>
    );
}

/* Exporting the Nav component for use in other parts of the application */
export default Nav;

/* link.json comments

tutorial referenced: https://youtu.be/D8fdAsIxDh8?si=2R3RB-d9MX9b1--O

{
  (Key contains an array of link objects)
  "links": [
    {
      ("label" represents the link text)
      "label": "Home",
      ("href" represents the URL the link points to)
      "href": "/home"
    }
  ]
} */
