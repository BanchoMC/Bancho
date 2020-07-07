// ____                   _
// |  _ \                 | |
// | |_) | __ _ _ __   ___| |__   ___
// |  _ < / _` | '_ \ / __| '_ \ / _ \
// | |_) | (_| | | | | (__| | | | (_) |
// |____/ \__,_|_| |_|\___|_| |_|\___/
//
// This is the configuration for Bancho. It contains configuration for the entire plugin and all of its components. It
// is written to be as configurable as possible so you can enable and disable what you want. Hell, it's so configurable
// you can just turn off all the modules and it'll be a waste of about 2MB of your RAM!

// Here's a quick rundown on what some of the terminology means:
// - All of the sections prefixed with 'component' relate to a component of Bancho. A component is a form of module that
//   is included with Bancho. 'component chat', for example, would mean this section is configuring the chat component.

// Without further ado, let's get configurating.

// Indicates that this instance of Bancho has not yet been configured.
firstRun = true

// Handles chat management
component "chat" {
  // Enable the chat component
  enabled = false

  // Chat formatting
  formatting {
    // Disable this if you have another plugin doing chat formatting
    enabled = true
    // The way the chat is formatted (color codes supported)
    format = "<$PREFIX$PLAYER$SUFFIX> $MESSAGE"
  }

  // Nickname configuration
  nick {
    // Prefix to put on a nickname to indicate this is a nickname
    prefix = "*"
    // Allows usernames to have spaces (like '*Epic Gamer')
    allowSpaces = true
  }
}