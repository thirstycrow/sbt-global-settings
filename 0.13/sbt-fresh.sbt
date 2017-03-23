import de.heikoseeberger.sbtfresh.FreshPlugin.autoImport._
import de.heikoseeberger.sbtfresh.license.License

freshOrganization := "org.github.thirstycrow" // Organization – "default" by default
freshAuthor       := "Jiang Hua"              // Author – value of "user.name" system property or "default" by default
freshLicense      := Some(License.apache20)   // Optional license – `apache20` by default
freshSetUpGit     := true                     // Initialize a Git repo and create an initial commit – `true` by default
freshSetUpTravis  := false                    // Configure Travis for Continuous Integration - `true` by default
freshUseGitPrompt := true                     // Use the prompt from the sbt-git plugin - `false` by default
