import * as React from "react";
import Card from "react-bootstrap/Card";
import Button from "react-bootstrap/Button";
import Accordion from "react-bootstrap/Accordion";
import Poll from "./Poll";
import {Link} from "react-router-dom";

export default class PollCard extends React.Component {

    render() {
        let pollCardStyle = {
            color: '#000000'
        };
        const link = `/computeresult/${this.props.pollId}`;
        return (

            <Accordion>
                <Card className="col-lg-12">
                    <Card.Body style={pollCardStyle}>
                        <Accordion.Toggle as={Button} variant="link" eventKey="0">{this.props.title}</Accordion.Toggle>
                        <Accordion.Collapse eventKey="0">
                            <div>
                                <Poll pollId={this.props.pollId}/>
                                <Link to={link}>Show votes</Link>
                            </div>
                        </Accordion.Collapse>
                    </Card.Body>
                </Card>
            </Accordion>
        )
    }

}