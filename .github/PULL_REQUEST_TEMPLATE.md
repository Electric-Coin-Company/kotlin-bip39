This code review checklist is intended to serve as a starting point for the author and reviewer, although it may not be appropriate for all types of changes (e.g. fixing a spelling typo in documentation).  For more in-depth discussion of how we think about code review, please see [Code Review Guidelines](../blob/main/docs/CODE_REVIEW_GUIDELINES.md).

# Author
<!-- NOTE: Do not modify these when initially opening the pull request.  This is a checklist template that you tick off AFTER the pull request is created. -->
- [ ] Self-review: Did you review your own code in GitHub's web interface? _Code often looks different when reviewing the diff in a browser, making it easier to spot potential bugs._
- [ ] Automated tests: Did you add appropriate automated tests for any code changes?
- [ ] Code coverage: Did you check the code coverage report for the automated tests?  _While we are not looking for perfect coverage, the tool can point out potential cases that have been missed. Run `./gradlew check` then coverage reports are generated under `build/reports/kover`.
- [ ] Documentation: Did you update documentation as appropriate? (e.g [README.md](../blob/master/README.md), etc.)
- [ ] Rebase and squash: Did you pull in the latest changes from the main branch and squash your commits before assigning a reviewer? _Having your code up to date and squashed will make it easier for others to review. Use best judgement when squashing commits, as some changes (such as refactoring) might be easier to review as a separate commit._


# Reviewer

- [ ] Checklist review: Did you go through the code with the [Code Review Guidelines](../blob/master/docs/CODE_REVIEW_GUIDELINES.md) checklist?
- [ ] Ad hoc review: Did you perform an ad hoc review?  _In addition to a first pass using the code review guidelines, do a second pass using your best judgement and experience which may identify additional questions or comments. Research shows that code review is most effective when done in multiple passes, where reviewers look for different things through each pass._
- [ ] Automated tests: Did you review the automated tests?
- [ ] Manual tests: Did you review the manual tests?
- [ ] How is code coverage affected by this PR? _We encourage you to compare coverage before and after changes and when possible, leaving it in a better place._
- [ ] Documentation: Did you review Docs and [README.md](../blob/master/README.md) as appropriate?
