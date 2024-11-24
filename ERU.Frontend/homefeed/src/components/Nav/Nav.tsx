import React from "react"; // Import React for JSX syntax and components
import styles from "./Nav.module.css"; // Import CSS module for styling
import data from "./links.json"; // Import navigation links from JSON file

// Define TypeScript interfaces
interface Link {
  href: string;
  label: string;
}

interface LinksProps {
  links: Link[];
}

// Logo reference (assumes it is in the public folder)
const logo = "/logo.png";

// Functional component to render the list of navigation links
const Links: React.FC<LinksProps> = ({ links }) => {
  return (
    <div className={styles["links-container"]}>
      {links.map((link) => (
        <div key={link.href} className={styles["link"]}>
          <a href={link.href}>{link.label}</a>
        </div>
      ))}
    </div>
  );
};

// Main navigation bar component
const Nav: React.FC = () => {
  // Extract the 'links' array from the JSON data
  const links: Link[] = data.links;

  return (
    <nav className={styles.navbar}>
      <div className={styles["logo-container"]}>
        <img src={logo} alt="logo" className={styles.logo} />
      </div>
      <Links links={links} />
    </nav>
  );
};

export default Nav;
